package com.zxn.wym.fireworks.widget;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zxn.wym.fireworks.bean.Firework;
import com.zxn.wym.fireworks.bean.Particle;
import com.zxn.wym.fireworks.util.MathUtil;

import java.util.*;


public class FireworkDrawable extends Drawable {
    private float w;
    private float h;
    private float groundH;
    private float boomMaxH;
    private float boomMinH;
    Paint mPaint = new Paint();
    private int fireworkCount = 4;
    private List<Firework> list = new ArrayList<>();
    private float gravityEffect = 10;

    public FireworkDrawable(float w, float h) {
        this.w = w;
        this.h = h;
        groundH = h / 8 * 7;
        boomMaxH = h / 4;
        boomMinH = h / 3;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        for (int i = 0; i < fireworkCount; i++) {
            list.add(createFirework());
        }
    }

    private Firework createFirework() {
        float startX = MathUtil.randomFloat(w / 8, w / 8 * 7);
        float startY = h / 8 * 7;
        float boomX = startX;
        float boomY = MathUtil.randomFloat(boomMaxH, boomMinH);
        PointF startPoint = new PointF(startX, startY);
        PointF boomPoint = new PointF(boomX, boomY);
        float initSpeed = (groundH - boomMaxH) / 1f;
        gravityEffect = initSpeed / 3;
        Firework firework = new Firework(startPoint, boomPoint, initSpeed, gravityEffect);
        return firework;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        List<Firework> removeList = new ArrayList<>();
        for (Firework firework : list) {
            if (firework.isBoom()){
                if (firework.getList().size() == 0){
                    removeList.add(firework);
                }
            }else{
                if (firework.getRealPoint().y<h/6||firework.getRealPoint().y>groundH){
                    removeList.add(firework);
                }
            }
        }
        if (removeList.size() > 0) {
            list.removeAll(removeList);
            for (int i = 0; i < removeList.size(); i++) {
                Firework firework = createFirework();
                list.add(firework);
            }
        }
        if (list.size() > 0) {
            for (Firework firework : list) {
                drawFirework(canvas, firework);
            }
            invalidateSelf();
        }
    }

    private void drawFirework(Canvas canvas, Firework firework) {
        if (firework.isBoom()) {
            List<Particle> removeList = new ArrayList<>();
            List<Particle> remainList = firework.getList();
            for (Particle particle : remainList) {
                int alpha = particle.getAlpha();
                if (alpha < 50) {
                    removeList.add(particle);
                }
            }
            ;
            remainList.removeAll(removeList);
            for (Particle particle : remainList) {
                particle.refresh();
                PointF trailStart = particle.getTrailStart();
                PointF realPoint = particle.getRealPoint();
                float initRadius = particle.getInitRadius();
                float moveAngle = particle.getMoveAngle();
                float verticalAngle = moveAngle + (float) Math.PI / 2;
                LinearGradient linearGradient = new LinearGradient(realPoint.x, realPoint.y, trailStart.x, trailStart.y,
                        Color.WHITE, particle.getInitColor(), Shader.TileMode.MIRROR);
                mPaint.setShader(linearGradient);
                mPaint.setStrokeWidth(2 * particle.getInitRadius() * 0.9f);
                mPaint.setColor(particle.getInitColor());
                Path path = new Path();
                path.moveTo(trailStart.x, trailStart.y);
                path.lineTo(realPoint.x + initRadius * (float) Math.cos(verticalAngle), realPoint.y + initRadius * (float) Math.sin(verticalAngle));
                path.lineTo(realPoint.x + initRadius * (float) Math.cos(verticalAngle + Math.PI), realPoint.y + initRadius * (float) Math.sin(verticalAngle + Math.PI));
                path.close();
                canvas.drawPath(path, mPaint);
                mPaint.setShader(null);
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(0);
                canvas.drawCircle(realPoint.x, realPoint.y, initRadius, mPaint);
            }
        } else {
            firework.onRefresh();
            PointF pointF = firework.getRealPoint();
            RadialGradient radialGradient = new RadialGradient(
                    pointF.x, pointF.y, firework.getInitRadius() * 2,
                    new int[]{ Color.WHITE, Color.WHITE, firework.getInitColor()}, new float[]{0f, 0.4f, 1f},
                    Shader.TileMode.MIRROR
            );
            mPaint.setShader(radialGradient);
            canvas.drawCircle(pointF.x, pointF.y, firework.getInitRadius() * 2, mPaint);
        }

    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) h;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) w;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}