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
	private int poolSize = 50;
	private int maxPoolSize = poolSize * 2;
	private int queueSize = 250;
	
	private long keepAliveTime = 10;
	
	private ThreadPoolExecutor executor = null;
	
	private final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSize);
	
	private static TaskManager instance;
	
	private TaskManager() {
		executor = new ThreadPoolExecutor(poolSize, maxPoolSize, keepAliveTime, TimeUnit.SECONDS, queue);
	}
	
	synchronized public static TaskManager getInstance() {
		if (instance == null) {
			instance = new TaskManager();
		}
		return instance;
	}
	
	public void runTask(Runnable task) {
		log.debug("Active Tasks: {}", executor.getActiveCount());
		executor.execute(task);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Future submit(Callable callable) {
		log.debug("Active Tasks: {}", executor.getActiveCount());
		return executor.submit(callable);
	}
	
	public void shutdown() {
		executor.shutdown();
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.shutdown();
		super.finalize();
	}

}
