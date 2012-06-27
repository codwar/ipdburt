/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package iddb.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskManager {

	private static final Logger log = LoggerFactory.getLogger(TaskManager.class);

	private final Integer DEFAULT_CORE_SIZE = 5;
	private final Integer DEFAULT_POOL_SIZE = 50;
	private final Integer DEFAULT_QUEUE_SIZE = 200;
	private final Long DEFAULT_KEEP_ALIVE = 5L;
	
	private final ArrayBlockingQueue<Runnable> queue;
	
	private ThreadPoolExecutor executor = null;
	
	private static TaskManager instance;
	
	private TaskManager() {
		queue = new ArrayBlockingQueue<Runnable>(DEFAULT_QUEUE_SIZE);
		executor = new ThreadPoolExecutor(DEFAULT_CORE_SIZE, DEFAULT_POOL_SIZE, DEFAULT_KEEP_ALIVE, TimeUnit.SECONDS, queue);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());	
	}
	
	synchronized public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}
	
	/**
	 * Execute a simple runnable task.
	 * If the queue is full the task is executed in the current Thread.
	 * @param Runnable
	 * @throws InterruptedException
	 */
	public synchronized void execute(Runnable task) throws InterruptedException {
		if (executor.isTerminating()) throw new InterruptedException();
		log.trace("execute");
		executor.execute(task);
		log.trace("Active Tasks: {} - Pool Size: {}", 
				new Object[]{executor.getActiveCount(), executor.getPoolSize()});
	}
	
	/**
	 * Execute a callable async task.
	 * If the queue is full the task is executed in the current Thread.
	 * @param Callable
	 * @return A Future instance referencing the submitted task.
	 * @throws InterruptedException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized Future submit(Callable task) throws InterruptedException {
		if (executor.isTerminating()) throw new InterruptedException();
		log.trace("submit");
		Future f = executor.submit(task);
		log.trace("Active Tasks: {} - Pool Size: {}", 
				new Object[]{executor.getActiveCount(), executor.getPoolSize()});
		return f;
	}
	
	public void shutdown() {
		executor.shutdown();
		executor.shutdownNow();
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.shutdown();
		super.finalize();
	}

}
