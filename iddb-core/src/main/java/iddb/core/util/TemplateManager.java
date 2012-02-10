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
package iddb.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class TemplateManager {

	public static String getTemplate(String name, Map<String, String> args) throws IOException, TemplateException {
		Configuration cfg = new Configuration();
		TemplateLoader loader = new ClassTemplateLoader(TemplateManager.class, "/templates");
		cfg.setTemplateLoader(loader);
		cfg.setEncoding(Locale.getDefault(), "ISO-8859-1");
		cfg.setObjectWrapper(new DefaultObjectWrapper());
		StringWriter out = new StringWriter();
		Template template;
		template = cfg.getTemplate(name + ".ftl");
		template.process(args, out);
		return out.toString();
	}
}
