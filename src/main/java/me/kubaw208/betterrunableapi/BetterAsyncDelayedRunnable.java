package me.kubaw208.betterrunableapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Classic asynchronous delayed task class that is executes after given delay.
 * Can be paused and unpaused.
 */
public class BetterAsyncDelayedRunnable extends BetterRunnable {

    private int passedTimeInMilliseconds = 0;
    private long taskStartedTime = 0;

    /**
     * Creates new asynchronous delayed task that is executed only once after given delay
     * @param plugin plugin main class that runs task
     * @param task code in task to run
     * @param delay time in milliseconds to wait before task executes code
     */
    public BetterAsyncDelayedRunnable(JavaPlugin plugin, Consumer<BetterRunnable> task, long delay) {
        super(plugin, task, delay, 0);
    }

    /**
     * Creates new asynchronous delayed task that is executed only once after given delay
     * @param plugin plugin main class that runs task
     * @param group tasks group that automatically adds task to that group
     * @param task code in task to run
     * @param delay time in milliseconds to wait before task executes code
     */
    public BetterAsyncDelayedRunnable(JavaPlugin plugin, BetterRunnableGroup group, Consumer<BetterRunnable> task, long delay) {
        super(plugin, group, task, delay, 0);
    }

    /**
     * Starts delayed task. Can be used again after {@link #run()} method to repeat code in task with the same delay
     */
    @Override
    public void startTask() {
        cancel();
        runnableID = Bukkit.getAsyncScheduler().runDelayed(getPlugin(), scheduledTask -> run(), getDelay() - passedTimeInMilliseconds, TimeUnit.MILLISECONDS);
        taskStartedTime = System.currentTimeMillis();
    }

    /**
     * Code in method, that executes when delayed task is done
     */
    @Override
    public void run() {
        super.run();
        passedTimeInMilliseconds = 0;
        taskStartedTime = 0;
    }

    /**
     * Pauses delayed task and saves the time already waited
     */
    @Override
    public void pause() {
        super.pause();
        cancel();
        passedTimeInMilliseconds += System.currentTimeMillis() - taskStartedTime;
    }

    /**
     * Unpauses delayed task and starts it with missing time to complete task in correct time
     */
    @Override
    public void unpause() {
        super.unpause();
        startTask();
    }

}