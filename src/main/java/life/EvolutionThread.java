package life;

public class EvolutionThread extends Thread {

    private final Runnable runnable;
    private int delay;
    private boolean paused;
    private boolean running;
    private final Object lock;

    public EvolutionThread(Runnable runnable, int delay) {
        super();
        this.runnable = runnable;
        this.delay = delay;
        paused = true;
        running = true;
        lock = new Object();
    }

    @Override
    public void run() {
        while (running) {
            try{
                if (paused) {
                    synchronized (lock) {
                        lock.wait();
                    }
                    if (!running) {
                        break;
                    }
                }
                Thread.sleep(delay);
                runnable.run();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void pauseEvolution() {
        paused = true;
        System.out.println("Pausing cell evolution");
    }

    public void stopEvolution() {
        running = false;
    }

    public void playEvolution() {
        paused = false;
        System.out.println("Evolving cells");
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }
}
