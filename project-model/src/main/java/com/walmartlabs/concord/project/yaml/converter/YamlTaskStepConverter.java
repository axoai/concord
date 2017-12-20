package com.walmartlabs.concord.project.yaml.converter;

/*-
 * *****
 * Concord
 * -----
 * Copyright (C) 2017 Wal-Mart Store, Inc.
 * -----
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =====
 */

import com.walmartlabs.concord.project.yaml.YamlConverterException;
import com.walmartlabs.concord.project.yaml.model.YamlTaskStep;
import io.takari.bpm.model.ExpressionType;
import io.takari.bpm.model.ServiceTask;
import io.takari.bpm.model.VariableMapping;

import java.util.Set;

public class YamlTaskStepConverter implements StepConverter<YamlTaskStep> {

    @Override
    public Chunk convert(ConverterContext ctx, YamlTaskStep s) throws YamlConverterException {
        Chunk c = new Chunk();

        Set<VariableMapping> inVars = getVarMap(s.getOptions(), "in");
        Set<VariableMapping> outVars = getVarMap(s.getOptions(), "out");

        String id = ctx.nextId();
        String expr = "${" + s.getKey() + "}";
        c.addElement(new ServiceTask(id, ExpressionType.DELEGATE, expr, inVars, outVars, true));
        c.addOutput(id);
        c.addSourceMap(id, toSourceMap(s, "Task: " + s.getKey()));

        applyErrorBlock(ctx, c, id, s.getOptions());

        return c;
    }

}
