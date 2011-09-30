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
package iddb.web.security;

import iddb.web.security.subject.AnonymousSubject;
import iddb.web.security.subject.Subject;


public class ThreadContext {

	private ThreadLocal<Subject> subject;
	//private ThreadContext instance;
	
	private class SubjectThreadLocal<T extends Subject> extends InheritableThreadLocal<Subject> {
		
		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected Subject initialValue() {
			return new AnonymousSubject();
		}
		
	}
	public ThreadContext() {
		subject = new SubjectThreadLocal<Subject>();
	}
	
//	private ThreadContext getInstance() {
//		if (instance == null) {
//			instance = new ThreadContext();			
//		}
//		return instance;
//	}
//	
	public Subject getSubject() {
		return this.subject.get();
	}
	
	public void setSubject(Subject subject) {
		this.subject.set(subject);
	}
	
	public void removeSubject() {
		this.subject.remove();
	}
	
}
