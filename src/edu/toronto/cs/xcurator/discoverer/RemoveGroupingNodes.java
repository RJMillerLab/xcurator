/*
 *    Copyright (c) 2013, University of Toronto.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License"); you may
 *    not use this file except in compliance with the License. You may obtain
 *    a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *    License for the specific language governing permissions and limitations
 *    under the License.
 */
package edu.toronto.cs.xcurator.discoverer;

import edu.toronto.cs.xcurator.common.DataDocument;
import edu.toronto.cs.xcurator.common.RdfUriBuilder;
import edu.toronto.cs.xcurator.mapping.Mapping;
import edu.toronto.cs.xcurator.mapping.Schema;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Amirhossein Aleyasen <aleyase2@illinois.edu>
 */
public class RemoveGroupingNodes implements MappingDiscoveryStep {

    private final RdfUriBuilder rdfUriBuilder;

    public RemoveGroupingNodes(RdfUriBuilder rdfUriBuilder) {
        this.rdfUriBuilder = rdfUriBuilder;
    }

    @Override
    public void process(List<DataDocument> dataDocuments, Mapping mapping) {
        System.out.println("process RemoveGroupingNodes...");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>> Mapping");
        System.out.println(mapping);
//        for (String xmlTypeUri : mapping.getEntities().keySet()) {
//            
//        }
// TODO: we should remove blank nodes to create minimal isomorphic graph.
        Iterator<Schema> it = mapping.getEntityIterator();
        while (it.hasNext()) {
            Schema entity = it.next();
            if (entity.getAttributesCount() == 0) {
                it.remove(); // this is equivalent to mapping.removeEntity(entity.getId()); we did that to prevent java.util.ConcurrentModificationException  
            }
        }
        // remove relations to the blank nodes that we just removed.
        mapping.removeInvalidRelations();
    }
}
