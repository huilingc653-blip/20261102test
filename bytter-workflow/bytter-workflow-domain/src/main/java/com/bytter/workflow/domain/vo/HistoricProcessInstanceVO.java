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
import java.io.Serializable;
import java.util.Date;
@Data
/**
 * @author Tijs Rademakers
 */
public class HistoricProcessInstanceVO implements Serializable {
    protected String id;
    protected String url;
    protected String businessKey;
    protected String processDefinitionId;
    protected String processDefinitionUrl;
    protected String processDefinitionName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected Date nowTime = new Date();
    protected Long durationInMillis;
    protected String startUserId;
    protected String startActivityId;
    protected String endActivityId;
    protected String deleteReason;
    protected String superProcessInstanceId;
    protected String tenantId;
    private String username;
    private String flowCategoryId;
    private String flowCategoryName;
    private String corpName;
    private String corpCode;
    private String messageTemplate;
}
