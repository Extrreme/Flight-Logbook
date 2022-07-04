package dev.extrreme.logbook.scheduling;

import java.util.UUID;
import java.util.concurrent.ScheduledFuture;

public class RepeatingTask {
	private final UUID id;

	private boolean cancelled;

	private Runnable runnable;
	private int initialDelay;
	private int interval;

	private ScheduledFuture<?> future;

	/**
	 * Creates a repeating task to be run via the {@link Scheduler}
	 * @param runnable the runnable to be run each cycle
	 * @param initialDelay the initial interval before first execution
	 * @param interval the interval between runnable executions
	 */
	public RepeatingTask(Runnable runnable, int initialDelay, int interval){
		this.id = UUID.randomUUID();
		this.cancelled = false;
		this.runnable = runnable;
		this.initialDelay = initialDelay;
		this.interval = interval;
	}

	/**
	 * Get the id of the task
	 * @return the {@link UUID uuid} of the task
	 */
	public UUID getId(){
		return this.id;
	}

	/**
	 * Get the runnable of this task
	 * @return the task runnable
	 */
	public Runnable getRunnable() {
		return this.runnable;
	}

	/**
	 * Set the runnable of this task
	 * @param runnable the runnable to be set as this tasks runnable
	 */
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

	/**
	 * Get the initial delay of this task
	 * @return the initial delay, as an integer
	 */
	public int getInitialDelay() {
		return this.initialDelay;
	}

	/**
	 * Set the repeating delay of this task
	 * @param initialDelay the delay to be set as this tasks delay
	 */
	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	/**
	 * Get the runnable execution interval
	 * @return the runnable execution interval, as an integer
	 */
	public int getInterval() {
		return this.interval;
	}

	/**
	 * Set the runnable execution interval
	 * @param interval the interval to be set as this tasks runnable execution interval
	 */
	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * Check if the task has been cancelled
	 * @return TRUE if the task was cancelled, FALSE otherwise
	 */
	public boolean isCancelled() {
		return this.cancelled;
	}

	/**
	 * Cancel the task
	 */
	public void cancel() {
		cancelled = true;
		if (future != null) {
			future.cancel(false);
		}
	}

	/**
	 * Get the future for this task
	 * @return the future for this task
	 */
	public ScheduledFuture<?> getFuture() {
		return this.future;
	}

	/**
	 * Ser the future for this task
	 * @param future the future to be set as the future for this task
	 */
	public void setFuture(ScheduledFuture<?> future) {
		this.future = future;
	}
}
