package mygame;

public class GameLoopManager implements Runnable {
    private final int fps;
    private final GameUpdateCallback updateCallback;
    private final GameRenderCallback renderCallback;
    private boolean running = true;
    private volatile boolean paused = false;
    private boolean needRedraw = true; // 添加缺失的变量

    public GameLoopManager(int fps, GameUpdateCallback update, GameRenderCallback render) {
        this.fps = fps;
        this.updateCallback = update;
        this.renderCallback = render;
    }
    
    public void pause() {
        paused = true;
        needRedraw = true; // 标记需要重绘暂停画面
    }
    
    public void resume() {
        paused = false;
    }
    
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double unprocessed = 0;
        double frameTime = 1_000_000_000.0 / fps;
        int frameCount = 0;
        long lastFPS = lastTime;

        while (running) {
            long now = System.nanoTime();
            unprocessed += (now - lastTime) / frameTime;
            lastTime = now;
            
            // 暂停处理
            if(paused) {
                if(needRedraw) {
                    renderCallback.render(); // 渲染暂停画面
                    needRedraw = false; // 重置重绘标记
                }
                try { 
                    Thread.sleep(100); // 降低CPU占用
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }
            
            // 游戏更新
            while (unprocessed >= 1) {
                updateCallback.update();
                unprocessed--;
            }
            
            // 渲染
            renderCallback.render();
            frameCount++;
            
            // FPS显示
            if(now - lastFPS >= 1_000_000_000) {
                renderCallback.setFPS(frameCount); // 使用接口方法
                frameCount = 0;
                lastFPS = now;
            }
            
            // 精确等待
            long target = lastTime + (long)frameTime;
            while(System.nanoTime() < target) {
                Thread.yield(); // 让出CPU时间片
            }
        }
    }
}