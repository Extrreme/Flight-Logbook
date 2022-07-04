package dev.extrreme.logbook.scheduling;

import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.*;

public class Scheduler {
	private static Scheduler instance;
	private final ExecutorService executor;
	private final ScheduledExecutorService repeater;
	private final WeakHashMap<UUID, RepeatingTask> repeatingTasks = new WeakHashMap<>();

	private Scheduler() { // Prevent external instantiation
		System.out.println("Initializing thread pool...");
		this.repeater = Executors.newScheduledThreadPool(4);
		this.executor = Executors.newCachedThreadPool();
	}

	/**
	 * Get an instance of the {@link Scheduler}, initializing a new one if not already initialized
	 * @return the scheduler
	 */
	public static Scheduler getInstance() {
		if (instance == null) {
			instance = new Scheduler();
		}
		return instance;
	}

	/**
	 * Start running a repeating task
	 * @param task the {@link RepeatingTask repeating task} to run
	 * @return The task, so that it can be stored and modified if needed
	 */
	public RepeatingTask runRepeatingAsyncTask(final RepeatingTask task) {
		repeatingTasks.put(task.getId(), task);
		task.setFuture(repeater.scheduleWithFixedDelay(task.getRunnable(), task.getInitialDelay(), task.getInterval(),
				TimeUnit.MILLISECONDS));
		return task;
	}

	/**
	 * Cancel all currently running repeating tasks
	 */
	public void cancelAll() {
		for (UUID id : repeatingTasks.keySet()) {
			RepeatingTask t = repeatingTasks.get(id);
			if (t != null) {
				cancelTask(t);
				continue;
			}
			repeatingTasks.remove(id);
		}
	}

	/**
	 * Cancel a repeating task
	 * @param task the {@link UUID id} of the task to cancel
	 */
	public void cancelTask(UUID task) {
		if (!repeatingTasks.containsKey(task)) {
			return;
		}
		RepeatingTask t = repeatingTasks.get(task);
		t.cancel();
		repeatingTasks.remove(task);
	}

	/**
	 * Cancel a repeating task
	 * @param task the {@link RepeatingTask repeating task} to cancel
	 */
	public void cancelTask(RepeatingTask task) {
		task.cancel();
		repeatingTasks.remove(task.getId());
	}

	/**
	 * Run a task asynchronously
	 * @param run the {@link Runnable runnable} to be run asynchronously
	 */
	public void runTaskAsynchronously(final Runnable run) {
		// Explicitly make sure task is terminated so threads can close
		executor.submit(run);
	}

	/**
	 * Shut down the scheduler
	 */
	public void shutdown(){
		executor.shutdown();
		repeater.shutdown();
		try {
			executor.awaitTermination(5, TimeUnit.SECONDS);
			repeater.awaitTermination(5, TimeUnit.SECONDS);
		} catch (InterruptedException ignored) {}
	}

	/**
	 * Run a task asynchronously after a specific delay
	 * @param run the {@link Runnable runnable} to be run asynchronously after the delay
	 * @param delay the delay before running the runnable, in millis
	 * @return the future for the task
	 */
	public ScheduledFuture<?> runTaskAsynchronouslyLater(final Runnable run, final int delay){
		return repeater.schedule(run, delay, TimeUnit.MILLISECONDS);
	}
}
