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
package edu.toronto.cs.xcurator.mapping;

import edu.toronto.cs.xcurator.common.NsContext;
import edu.toronto.cs.xcurator.utils.IOUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author zhuerkan
 */
public class XmlBasedMapping implements Mapping {

    private boolean initialized;

    private String namespaceUri;

    private NsContext baseNamespaceContext;

    private Map<String, Schema> entities;

    public final String tagNamePrefix;

    public XmlBasedMapping() {
        this("http://www.cs.toronto.edu/xcurator", "xcurator");
    }

    public XmlBasedMapping(String namespaceUri, String tageNamePrefix) {
        this.namespaceUri = namespaceUri;
        this.tagNamePrefix = tageNamePrefix;
        entities = new HashMap<>();
        baseNamespaceContext = new NsContext();
    }

    @Override
    public Map<String, Schema> getEntities() {
        return entities;
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean setInitialized() {
        if (baseNamespaceContext == null) {
            return false;
        }
        if (namespaceUri == null) {
            return false;
        }
        return initialized = true;
    }

    public void setMappingNamespaceUri(String uri) {
        namespaceUri = uri;
    }

    public String getMappingNamespaceUri() {
        return namespaceUri;
    }

    @Override
    public void setBaseNamespaceContext(NsContext nsContext) {
        this.baseNamespaceContext = nsContext;
    }

    @Override
    public NsContext getBaseNamespaceContext() {
        return baseNamespaceContext;
    }

    @Override
    public void addEntity(Schema entity) {
        entities.put(entity.getXmlTypeUri(), entity);
    }

    @Override
    public Schema getEntity(String xmlTypeUri) {
        return entities.get(xmlTypeUri);
    }

    @Override
    public void removeEntity(String xmlTypeUri) {
        entities.remove(xmlTypeUri);
    }

    @Override
    public void removeInvalidRelations() {
        Iterator<Schema> it = getEntityIterator();
        while (it.hasNext()) {
            Schema entity = it.next();
            Iterator<Relation> relationIterator = entity.getRelationIterator();
            while (relationIterator.hasNext()) {
                Relation rel = relationIterator.next();
                if (!isValidRelation(rel, entities)) {
                    relationIterator.remove();
                }
            }
        }
    }

    @Override
    public Iterator<Schema> getEntityIterator() {
        return entities.values().iterator();
    }

    public static final String mappingTagName = "mapping";
    public static final String entityTagName = "entity";
    public static final String attributeTagName = "attribute";
    public static final String relationTagName = "relation";
    public static final String referenceTagName = "reference";
    public static final String idTagName = "id";
    public static final String keyAttrName = "key";
    public static final String nameAttrName = "name";
    public static final String xmlTypeAttrName = "xmlType";
    public static final String typeAttrName = "type";
    public static final String pathAttrName = "path";
    public static final String targetEntityXmlTypeAttrName = "targetEntityXmlType";
    public static final String referencePathAttrName = "path";
    public static final String referenceTargetPathAttrName = "targetPath";

    public String getMappingNodeName() {
        return tagNamePrefix + ":" + mappingTagName;
    }

    public String getEntityNodeName() {
        return tagNamePrefix + ":" + entityTagName;
    }

    public String getAttributeNodeName() {
        return tagNamePrefix + ":" + attributeTagName;
    }

    public String getRelationNodeName() {
        return tagNamePrefix + ":" + relationTagName;
    }

    public String getReferenceNodeName() {
        return tagNamePrefix + ":" + referenceTagName;
    }

    public String getIdNodeName() {
        return tagNamePrefix + ":" + idTagName;
    }

    @Override
    public String toString() {
        return "{ \"XmlBasedMapping\": "
                + "{"
                + "\"initialized\":" + "\"" + initialized + "\""
                + ", \"namespaceUri\":" + "\"" + namespaceUri + "\""
                + ", \"baseNamespaceContext\":" + "\"" + baseNamespaceContext + "\""
                + ", \"entities\":" + IOUtils.printMapAsJson(entities)
                + ", \"tagNamePrefix\":" + "\"" + tagNamePrefix + "\""
                + '}'
                + '}';
    }

    private boolean isValidRelation(Relation rel, Map<String, Schema> entities) {
        boolean validObject = false;
        boolean validSubject = false;
        for (Schema ent : entities.values()) {
            if (rel.object.equals(ent)) {
                validObject = true;
            }
            if (rel.subject.equals(ent)) {
                validSubject = true;
            }
        }
        return validObject && validSubject;
    }

}
