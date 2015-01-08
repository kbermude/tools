/**
 * Copyright (c) 2011 Source Auditor Inc.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
*/
package org.spdx.rdfparser.license;

import java.util.ArrayList;

import org.spdx.rdfparser.InvalidSPDXAnalysisException;
import org.spdx.rdfparser.RdfParserHelper;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * This abstract class represents several ways of describing licensing information.
 * License info can be described as a set of conjunctive licenses (where all licenses
 * terms must apply), a set of disjunctive licenses (where there is a choice of one
 * license among the set described) or a specific license.  The specific licenses
 * are of a SimpleLicensingInfoType
 * @author Gary O'Neall
 *
 */
public abstract class AnyLicenseInfo implements Cloneable {
	
	Model model = null;
	Node licenseInfoNode = null;
	Resource resource = null;
	/**
	 * Create a new LicenseInfo object where the information is copied from
	 * the model at the LicenseInfo node
	 * @param model
	 * @param licenseInfoNode
	 * @throws InvalidSPDXAnalysisException 
	 */
	AnyLicenseInfo(Model model, Node licenseInfoNode) throws InvalidSPDXAnalysisException {
		this.model = model;
		this.licenseInfoNode = licenseInfoNode;
		resource = RdfParserHelper.convertToResource(model, licenseInfoNode);
	}


	AnyLicenseInfo() {
		this.model = null;
		this.licenseInfoNode = null;
		this.resource = null;
	}
	
	/**
	 * If a resource does not already exist in this model for this object,
	 * create a new resource and populate it.  If the resource does exist,
	 * return the existing resource.
	 * @param model
	 * @return resource created from the model
	 * @throws InvalidSPDXAnalysisException 
	 */
	public Resource createResource(Model model) throws InvalidSPDXAnalysisException {	
		if (this.model != null &&
				(this.model.equals(model) 
						|| (this.licenseInfoNode.isURI()))	// Remove this line to cause the spdx listed license 
				&&											// references to reference the URL rather than copy all of the properties into this model
				this.licenseInfoNode != null &&
				this.resource != null) {
			return resource;
		} else {
			this.model = model;
			Resource retval = _createResource(model);
			this.licenseInfoNode = retval.asNode();
			this.resource = retval;
			return retval;
		}
	}
	
	/**
	 * Internal implementation of create resource which is subclass specific
	 * @param model
	 * @return
	 * @throws InvalidSPDXAnalysisException 
	 */
	protected abstract Resource _createResource(Model model) throws InvalidSPDXAnalysisException;
	
	// force subclasses to implement toString
	public abstract String toString();
	
	// force subclasses to implement equals
	public abstract boolean equals(Object o);
	
	// force subclasses to implement hashcode
	public abstract int hashCode();

	/**
	 * @return
	 */
	public abstract ArrayList<String> verify();

	/**
	 * @return
	 */
	public Resource getResource() {
		return this.resource;
	}
	
	public abstract AnyLicenseInfo clone();
}
