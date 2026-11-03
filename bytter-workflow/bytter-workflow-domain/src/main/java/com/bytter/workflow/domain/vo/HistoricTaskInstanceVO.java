/* Licensed under the Apache License, Version 2.0 (the "License");
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
 */
package com.bytter.workflow.domain.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;
@Data
public class HistoricTaskInstanceVO {
    protected String id;
    protected String processDefinitionId;
    protected String processDefinitionUrl;
    protected String processInstanceId;
    protected String processInstanceUrl;
    protected String executionId;
    protected String name;
    protected String description;
    protected String deleteReason;
    protected String owner;
    protected String assignee;
    protected Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date endTime;
    protected Long durationInMillis;
    protected Long workTimeInMillis;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date claimTime;
    protected String taskDefinitionKey;
    protected String formKey;
    protected Integer priority;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date dueDate;
    protected String parentTaskId;
    protected String url;
    //    protected List<RestVariable> variables = new ArrayList<RestVariable>();
    protected String tenantId;
    protected String category;
}
