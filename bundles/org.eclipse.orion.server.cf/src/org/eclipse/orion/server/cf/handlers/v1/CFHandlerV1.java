/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.orion.server.cf.handlers.v1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.core.runtime.*;
import org.eclipse.orion.internal.server.servlets.ServletResourceHandler;
import org.eclipse.orion.server.cf.objects.Info;
import org.eclipse.orion.server.cf.objects.Target;

/**
 * A Jazz handler for Orion CF API v 1.0.
 */
public class CFHandlerV1 extends ServletResourceHandler<String> {

	private ServletResourceHandler<String> targetHandlerV1;
	private ServletResourceHandler<String> infoHandlerV1;

	public CFHandlerV1(ServletResourceHandler<IStatus> statusHandler) {
		targetHandlerV1 = new TargetHandlerV1(statusHandler);
		infoHandlerV1 = new InfoHandlerV1(statusHandler);
	}

	@Override
	public boolean handleRequest(HttpServletRequest request, HttpServletResponse response, String jazzPathInfo) throws ServletException {

		String pathString = jazzPathInfo;
		String[] infoParts = jazzPathInfo.split("\\/", 3); //$NON-NLS-1$

		if (infoParts.length >= 3) {
			pathString = infoParts[2];
			if (request.getContextPath().length() != 0) {
				IPath path = pathString == null ? Path.EMPTY : new Path(pathString);
				IPath contextPath = new Path(request.getContextPath());
				if (contextPath.isPrefixOf(path)) {
					pathString = path.removeFirstSegments(contextPath.segmentCount()).toString();
				}
			}
		}

		if (infoParts[1].equals(Target.RESOURCE)) {
			return targetHandlerV1.handleRequest(request, response, pathString);
		} else if (infoParts[1].equals(Info.RESOURCE)) {
			return infoHandlerV1.handleRequest(request, response, pathString);
		}

		return false;
	}
}
