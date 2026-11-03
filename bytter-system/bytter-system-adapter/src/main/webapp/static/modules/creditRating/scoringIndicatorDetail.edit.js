var $, form, selectType, dict, moneyType, select, rowStr = 0, isFirst = true;
bt.use(['form', 'selectType', 'dicts', 'select', 'moneyType', 'crScoringDataSource', 'element'], function () {
    $ = bt.jquery;
    form = bt.form();
    selectType = bt.selectType.init();
    moneyType = bt.moneyType.init();
    var element = layui.element;
    if ($("#rubicNum").val() > 0) {
        rowStr = $("#rubicNum").val() - 1//初始化评分细则数量
    }

    var crScoringDataSource = bt.crScoringDataSource.init();// 数据源组件
    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }

    //监听提交
    $("#submit").on("click", function () {
        bt.loading.show();
        arr = []
        //前端自定义校验是否通过标记
        var verifyPass = true;
        var scoringMethod = dict.get("scoringMethod").val();
        var indexProperty = dict.get("indexProperty").val();
        if (indexProperty != 2) {
            if (scoringMethod != 1) {
                $.each($('#scoringRubricTable1 tbody tr'), function () {
                    var obj = {
                        valueDirection: $(this).find('select').val(),
                        value: $(this).find('.scoringStandard').siblings('input').val(),
                        grade: $(this).find('.grade').siblings('input').val()
                    }
                    if (obj) {
                        if (!obj.valueDirection || obj.valueDirection == '') {
                            parent.layer.msg("评分符号不能为空", {icon: 2});
                            verifyPass = false;
                        }
                        if (!obj.value || obj.value == '') {
                            parent.layer.msg("评分条件值不能为空", {icon: 2});
                            verifyPass = false;
                        }
                        if (!obj.grade || obj.grade == '') {
                            parent.layer.msg("评分分值不能为空", {icon: 2});
                            verifyPass = false;
                        }
                    }

                    arr.push(obj);
                })
            } else {
                $.each($('#scoringRubricTable2 tbody tr'), function () {
                    var obj = {
                        valueDirection: $(this).find('select').val(),
                        grade: $(this).find('.onlyGrade').siblings('input').val()
                    }
                    if (obj) {
                        if (!obj.valueDirection || obj.valueDirection == '') {
                            parent.layer.msg("评分符号不能为空", {icon: 2});
                            verifyPass = false;
                        }
                        if (!obj.grade || obj.grade == '') {
                            parent.layer.msg("评分分值不能为空", {icon: 2});
                            verifyPass = false;
                        }
                        if (!obj.grade || obj.grade <= 0) {
                            parent.layer.msg("归一法的分值不能小于等于0", {icon: 2});
                            verifyPass = false;
                        }
                    }
                    arr.push(obj);
                })
            }
        } else {
            $("#dataSourceName").val("")
            $("#dataSourceId").val("")
            $("#dataSourceOrigin").val("")
            $("#dataSourceCode").val("")
        }
        if (verifyPass) {
            var isPass = bt.form().verifyFun("#searchForm");
            var isPassScoringRubricTable = bt.form().verifyFun("#scoringRForm");
            var action = "/creditRating/crScoringIndicator/modify"
            if (isPass && isPassScoringRubricTable) {
                var data = getFormFields("#searchForm");
                $.ajax({
                    url: basePath + action,
                    type: 'POST',
                    contentType: 'application/json', // 设置为JSON格式
                    data: JSON.stringify(getParam(arr, data)), // 将对象转换为JSON字符串
                    dataType: 'json',
                    success: function (result) {
                        bt.loading.hide();
                        if (result.success) {
                            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
                            if (bt.navtab) {
                                bt.navtab.getParentDom().$('[type="submit"]').trigger('click');
                                bt.navtab.closeCurrent();
                            } else {
                                parent.layer ? parent.layer.closeAll() : layer.closeAll();
                            }
                        } else {
                            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
                        }
                    },
                });
            } else {

            }
        }else{
            bt.loading.hide()
        }

        return false;
    })

    /**
     * 控制评分条件的符号和值输入
     * @param thisRowNum 当前编辑行号
     */
    function controlScoringRubric(thisRowNum){
        //评分方法
        var scoringMethod = dict.get("scoringMethod").val()
        //获取评分条件的所有行
        var trs = $("#scoringRubricTable1 tbody tr");
        var lastNum = trs.length - 1;
        //遍历所有行
        $.each(trs, function (index, item) {
            //获取当前遍历的行号
            rowNum = $(item).attr("data-row");
            //当前遍历的符号
            var scoringSymbol = dict.get("direction" + rowNum).val();
            //当前遍历的值
            var thisValue = moneyType.get("scoringStandard" + rowNum).val();
            //将每一行的值存入隐藏的输入框，方便后续传值给后端
            $("#value" + rowNum).val(thisValue)
            $("#valueDirection" + rowNum).val(scoringSymbol)
            var filterData1 = '';
            //第一行不需要做限制
            if (rowNum > 0) {
                //获取上一行的符号
                var preScoringSymbol = dict.get("direction" + (rowNum - 1)).val();
                //获取上一行的值
                var preValue = moneyType.get("scoringStandard" + (rowNum - 1)).val();
                //如果评分方法不为插值法
                if (scoringMethod != 3) {
                    //过滤等号
                    filterData1 += '3,'
                    //当前遍历的行号大于当前编辑的行号，意思就是只控制当前编辑以下的行
                    if (rowNum >= thisRowNum) {
                        if (preScoringSymbol == 1) {//上一行选择>
                            filterData1 += "4,6"
                            if (scoringSymbol == 1) {//当前选择>
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-max",parseFloat(preValue) - 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",0)
                                if(parseFloat(thisValue) >= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) - 1)
                                }
                            } else if (scoringSymbol == 2) {//当前选择>=
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-max",parseFloat(preValue))
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",0)
                                if(parseFloat(thisValue) > parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue))
                                }
                            }

                        } else if (preScoringSymbol == 2) {//上一行选择>=
                            filterData1 += "5,6"
                            if (scoringSymbol == 1 || scoringSymbol == 2) {//当前选择>
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-max",parseFloat(preValue) - 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",0)
                                if(parseFloat(thisValue) >= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) - 1)
                                }
                            }

                        } else if (preScoringSymbol == 4) {//上一行选择<
                            if (scoringSymbol == 4) {//当前选择<
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",parseFloat(preValue) + 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", '')
                                if(parseFloat(thisValue) <= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) + 1)
                                }
                            } else if (scoringSymbol == 5) {//当前选择<=
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",parseFloat(preValue))
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", '')
                                if(parseFloat(thisValue) < parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue))
                                }
                            }
                            filterData1 += "1,6"
                        } else if (preScoringSymbol == 5) {//上一行选择<=
                            if (scoringSymbol == 4 || scoringSymbol == 5) {//当前选择<,<=
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",parseFloat(preValue) + 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", '')
                                if(parseFloat(thisValue) <= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) + 1)
                                }
                            }
                            filterData1 += "2,6"
                        } else {
                            filterData1 += "6"
                        }
                        if (rowNum > thisRowNum) {
                            dict.get("direction" + rowNum).attr({"data-filterdata": filterData1})
                        }else{
                            //如果此行符号和上一行符号相反，将新增按钮隐藏
                            if (((preScoringSymbol == 1 || preScoringSymbol == 2) && (scoringSymbol == 4 || scoringSymbol == 5)) ||
                                ((preScoringSymbol == 4 || preScoringSymbol == 5) && (scoringSymbol == 1 || scoringSymbol == 2))) {

                                moneyType.get("scoringStandard" + rowNum).attr("data-max",preValue);
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",preValue);
                                //把上一行的值带下来，并且不让修改
                                moneyType.get("scoringStandard" + rowNum).val(preValue);
                                moneyType.get("scoringStandard" + rowNum).attr("readonly", "readonly");
                                $('[name = "add' + (rowNum) + '"]').hide();
                                //显示删除按钮
                                $('[name = "del' + (rowNum) + '"]').show();
                                //并且删除此行下面的所有行
                                for (var i = (parseInt(rowNum) + 1); i < trs.length; i++) {
                                    $(trs[i]).remove();
                                    rowStr--;
                                }
                            }else{
                                $('[name = "add' + (lastNum) + '"]').show();
                                moneyType.get("scoringStandard" + rowNum).removeAttr("readonly");
                            }
                        }
                    }
                } else {//如果是插值法
                    //需要知道第一行的符号方向，来确定最后一行的符号限制，第一行一旦确定了方向，如果中间有若干等于，那么等于后面的行数只能是等于或者反向
                    var firstDirection = dict.get("direction0").val();
                    //符号过滤掉满分
                    filterData1 += '6,'
                    //当前遍历行>=编辑行是为了控制编辑行的条件值(需要等于才能判断的到当前行)和下一行的符号(大于控制下一行)
                    if (rowNum >= thisRowNum) {
                        if (preScoringSymbol == 1) {//上一行选择>
                            filterData1 += "4"
                            if (scoringSymbol == 1) {//当前选择>
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", parseFloat(preValue) - 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",0)

                                if(parseFloat(thisValue) >= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) - 1)
                                }
                            } else if (scoringSymbol == 2  || scoringSymbol == 3) {//当前选择>=
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", parseFloat(preValue))
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",0)
                                if(parseFloat(thisValue) > parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue))
                                }
                            }
                        } else if (preScoringSymbol == 2) {//上一行选择>=
                            filterData1 += "5"
                            if (scoringSymbol == 1 || scoringSymbol == 2  || scoringSymbol == 3) {//当前选择>
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", parseFloat(preValue) - 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-min",0)
                                if(parseFloat(thisValue) >= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) - 1)
                                }
                            }
                        } else if (preScoringSymbol == 4) {//上一行选择<
                            filterData1 += "1"
                            //限制最小值为上一行的值
                            if (scoringSymbol == 4) {//当前选择<
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-min", parseFloat(preValue) + 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", '')
                                if(parseFloat(thisValue) <= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) + 1)
                                }
                            } else if (scoringSymbol == 5  || scoringSymbol == 3) {//当前选择<=
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-min", parseFloat(preValue))
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", '')
                                if(parseFloat(thisValue) < parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue))
                                }
                            }
                        } else if (preScoringSymbol == 5) {//上一行选择<=
                            filterData1 += "2"
                            //限制最小值为上一行的值加一
                            if (scoringSymbol == 4 || scoringSymbol == 5 || scoringSymbol == 3) {//当前选择<,<=
                                //限制最大值为上一行的值
                                moneyType.get("scoringStandard" + rowNum).attr("data-min", parseFloat(preValue) + 1)
                                moneyType.get("scoringStandard" + rowNum).attr("data-max", '')
                                if(parseFloat(thisValue) <= parseFloat(preValue)){
                                    moneyType.get("scoringStandard" + rowNum).val(parseFloat(preValue) + 1)
                                }
                            }
                        } else if (preScoringSymbol == 3) {//上一行选择=
                            //如果第一行选择>或>=
                            if (firstDirection == 1 || firstDirection == 2) {
                                //而当前行选择<，自动带出前一行的值
                                if (scoringSymbol == 4) {
                                    moneyType.get("scoringStandard" + rowNum).val(preValue)
                                } else {
                                    //判断上一行的条件值是否大于等于这一行
                                    if (parseFloat(thisValue) >= parseFloat(preValue)) {
                                        //提示上一行和此行的数值不能相等
                                        parent.layer.alert("[<=]对应的条件数值不能大于等于[=]的条件数值", {icon: 2});
                                        //将此行的条件值重新置为空
                                        moneyType.get("scoringStandard" + (rowNum)).val('');
                                    }
                                }
                                //控制符号只能选择<,<=,=
                                filterData1 += "1,2,6"
                            } else if (firstDirection == 4 || firstDirection == 5) {//如果第一行选择<或<=
                                //而当前行选择>，自动带出前一行的值
                                if (scoringSymbol == 1) {
                                    moneyType.get("scoringStandard" + rowNum).val(preValue)
                                } else {
                                    //判断上一行的条件值是否小于等于这一行
                                    if (parseFloat(thisValue) <= parseFloat(preValue)) {
                                        //提示上一行和此行的数值不能相等
                                        parent.layer.alert("[>=]对应的条件数值不能小于于等于[=]的条件数值", {icon: 2});
                                        moneyType.get("scoringStandard" + (rowNum)).val('');
                                    }
                                }
                                //控制符号只能选择<,<=,=
                                filterData1 += "4,5,6"
                            }
                        }
                        //只修改下一行的符号限制,这里不能等于，否则编辑行的符号会被清空
                        if (rowNum > thisRowNum) {
                            dict.get("direction" + rowNum).attr({"data-filterdata": filterData1})
                        }
                        //如果此行符号和第一行符号相反
                        if (((preScoringSymbol == 1 || preScoringSymbol == 2) && (scoringSymbol == 4 || scoringSymbol == 5)) ||
                            ((preScoringSymbol == 4 || preScoringSymbol == 5) && (scoringSymbol == 1 || scoringSymbol == 2))) {

                            moneyType.get("scoringStandard" + rowNum).attr("data-max",preValue);
                            moneyType.get("scoringStandard" + rowNum).attr("data-min",preValue);
                            //把上一行的值带下来，并且不让修改
                            moneyType.get("scoringStandard" + rowNum).val(preValue);
                            moneyType.get("scoringStandard" + rowNum).attr("readonly", "readonly");
                            $('[name = "add' + (rowNum) + '"]').hide();
                            //显示删除按钮
                            $('[name = "del' + (rowNum) + '"]').show();
                            //并且删除此行下面的所有行
                            for (var i = (parseInt(rowNum) + 1); i < trs.length; i++) {
                                $(trs[i]).remove();
                                rowStr--;
                            }
                        }else{
                            moneyType.get("scoringStandard" + rowNum).removeAttr("readonly");
                        }
                    }
                }
            }
        });
        dict.render()
        moneyType.render()
        form.render();
    }
    /**
     * 修改评分细则的符号触发的回调
     * @param data
     */
    bt.changeDirection = function (data) {
        //当前编辑的这一行
        var thisRowNum = $(data.elem).closest("tr").attr("data-row");
        controlScoringRubric(thisRowNum)
    }
    dict = bt.dicts.init();

    /**
     * 修改评分细则的评分触发的回调
     */
    $(document).on('change', 'input.scoring-standard', function (data) {
        var scoringMethod = dict.get("scoringMethod").val();
        if(scoringMethod != '5'){
            //获取当前编辑的行号
            var thisRow = $(data.target).closest("tr").attr("data-row");
            controlScoringRubric(thisRow);
        }
    })

    afterIndexProperty($("#indexPropertyCopy").val())//编辑界面进来，判断指标性质
    /**
     * 如果是定量
     */
    if (dict.get("indexProperty").val() == 1) {
        afterIsAutoScoringCallback($("#isAutoScoringCopy").val())//编辑界面进来，判断是否自动评分
        replaceScoringRubric($("#scoringMethodCopy").val())//编辑界面进来，判断评分方法
    }
    //标记是否是第一次加载编辑页面，默认为true
    isFirst = false;
});

function getParam(crScoringStandardVoList, data) {
    var id = $("#id").val();
    var parentId = $("#parentId").val();
    var scoringIndicatorCode = $("#scoringIndicatorCode").val();
    var scoringIndicatorName = $("#scoringIndicatorName").val();
    var status = dict.get("status").val();
    var indexProperty = dict.get("indexProperty").val();
    var isAutoScoring = dict.get("isAutoScoring").val();
    //如果是定性指标
    if (indexProperty == 2) {
        //自动评分改成否
        isAutoScoring = 2;
    }
    var scoringMethod = dict.get("scoringMethod").val();
    var isCheck = $("#isAddOrSubGrade").prop('checked')
    var isAddOrSubGrade = 0;
    if (isCheck) {
        isAddOrSubGrade = 1;
    }
    var dataSourceId = $("#dataSourceId").val();
    var dataSourceCode = $("#dataSourceCode").val();
    var dataSourceName = $("#dataSourceName").val();
    var dataSourceOrigin = $("#dataSourceOrigin").val();
    var scoringRubric = data.scoringRubric;
    var scoringIndicatorDescription = data.scoringIndicatorDescription;
    var param = {
        id: id,
        parentId: parentId,
        scoringIndicatorCode: scoringIndicatorCode,
        scoringIndicatorName: scoringIndicatorName,
        status: status,
        indexProperty: indexProperty,
        isAutoScoring: isAutoScoring,
        scoringMethod: scoringMethod,
        isAddOrSubGrade: isAddOrSubGrade,
        dataSourceId: dataSourceId,
        dataSourceCode: dataSourceCode,
        dataSourceName: dataSourceName,
        dataSourceOrigin: dataSourceOrigin,
        scoringRubric: scoringRubric,
        scoringIndicatorDescription: scoringIndicatorDescription,
        crScoringStandardVoList: crScoringStandardVoList
    };
    return param;
}

/**
 * 新增评分细则
 * @returns {boolean}
 */
function add() {
    rowStr++;
    html = '<tr data-row="' + rowStr + '">';
    html += '<td width="55%" class="input-50">'
    html += '<div class="bt-biz-dict direction" data-type="select" data-group="credit_scoring_direciton"data-name="direction' + rowStr + '" ' +
        'id="direction' + rowStr + '" data-biz-name="direction' + rowStr + '" data-required="true" data-callback="bt.changeDirection"></div>'
    html += '<input type="text" name="scoringStandard' + rowStr + '" id="scoringStandard' + rowStr + '" data-biz-name="scoringStandard' + rowStr + '" ' +
        'data-name="scoringStandard' + rowStr + '"' +
        ' autocomplete="off" class="layui-input bt-biz-moneyType scoring-standard scoringStandard"  style="float: right" >'
    html += '</td>'
    html += ' <td width="30%" align="center">'
    html += '<input type="text" name="grade' + rowStr + '" id="grade' + rowStr + '" data-name="grade' + rowStr + '" autocomplete="off" ' +
        'class="layui-input bt-biz-moneyType grade" data-biz-name="grade' + rowStr + '">'
    html += ' </td>'
    html += ' <td align="center">'
    html += '  <a  class="operateButtton" href="javascript:void(0)" name="add' + rowStr + '" class="layui-btn layui-btn-mini layui-btn-normal" onclick="add()">新增</a><sapn>     </sapn>'
    html += '<a  class="operateButtton" href="javascript:void(0)" name="del' + rowStr + '" class="layui-btn layui-btn-mini layui-btn-normal" onclick="del(this)">删除</a>'
    html += '</td>'
    html += '</tr>'

    $("#scoringRubricTable1 tbody tr:last").after(html);
    dict.render();
    moneyType.render();
    afterAdd(rowStr)

    return false;
}

/**
 * 删除评分细则
 * @param el
 */
function del(el) {
    $(el).parents('tr').remove();
    rowStr--;
    if (rowStr == 0) {
        $('[name = "add' + (rowStr) + '"]').show();
    } else {
        $('[name = "add' + (rowStr) + '"]').show();
        $('[name = "del' + (rowStr) + '"]').show();
    }
    form.render()
}

/**
 * 是否自动评分回调
 * @param data
 */
function isAutoScoringCallback(data) {
    afterIsAutoScoringCallback(data.value)
    // data.value == 1 是
    // data.value == 0 否
    if (data.value == 1) {
        // dataSourceName
        $("#dataSourceName").attr('lay-verify', "required")
        $(".change-show").removeClass('layui-hide')
    } else {
        $(".change-show").addClass('layui-hide')
        $("#dataSourceName").removeAttr('lay-verify')
        $("#dataSourceName").val("")
        $("#dataSourceId").val("")
        $("#dataSourceOrigin").val("")
        $("#dataSourceCode").val("")
    }
    form.renderIcon()
}

/**
 * 判断自动评分后处理
 * @param value
 */
function afterIsAutoScoringCallback(value) {
    var filterData = ''
    if (value == 0) {
        filterData += '1,4,5'
        dict.get("scoringMethod").attr({"data-filterdata": filterData})
    } else {
        dict.get("scoringMethod").attr({"data-filterdata": filterData});
    }
    dict.render()
    form.render()
}

/**
 * 评分方法触发的回调方法
 * @param data
 */
function scoringMethodCallback(data) {
    replaceScoringRubric(data.value);
}

/**
 * 判断评分方法后处理
 * @param scoringMethod
 */
function replaceScoringRubric(scoringMethod) {

    var text;
    var html = '';
    //定义是否需要去除必填校验的标记
    var isRequire1 = true;
    var isRequire2 = true;

    if (!isFirst) {
        clearScoringRubricTable("scoringRubricTable1");
        clearScoringRubricTable("scoringRubricTable2");
    }
    if (dict.get("indexProperty").val() != 2) {
        if (!scoringMethod || scoringMethod == " ") {//如果评分方法为空，那么默认为归一法
            $("#scoringR").hide();
        } else {
            $("#scoringR").show();
        }
    }
    if (scoringMethod == 1) {
        text = '评分方法说明：以授信额度为例，获取评价任务中评价机构的最高授信额度、最低授信额度，机构得分=“(该组织的<br>授信额度-最低授信额度)/(最高授信额度-最低授信额度)*满分分值';
        $("#remind").html(text)
        html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
        html += '<tr><td>' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> 满分 </div></td><td width="100px;">10</td></tr> '
        html += '</tbody></table>'
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").show();
        $("#description").hide()
        $("#example").show()
        //默认满分
        dict.get("direction0").attr({"data-filterdata": '1,2,3,4,5',"data-disabled":true})
        dict.get("direction0").val("6")
        isRequire1 = false;
        isRequire2 = true;
    } else if (scoringMethod == 2) {
        var text = ' 评分方法说明：以授信额度为例，将授信额度进行分段并匹配对应区间分值。系统获取评价机构的授信额度，根据授信额<br>度坐落区间取该评价机构得分。<br>'
            + '假设评分细则为：<br> 1.授信额度≥20亿：10分<br>2.10亿≤授信额度＜20亿：8分<br>3.5亿≤授信额度＜10亿：6分<br>4.授信额度＜5亿：4分<br>若评价机构授信额度为15亿，则其得分为8分；'
        $("#remind").html(text)
        html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
        html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 2000000000 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 1000000000 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 500000000 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 0.00 </div></td><td width="100px;">4</td></tr>'
        html += '</tbody></table>'
        $("#scoringRubricTable1").show();
        //第一行不能选择满分或者等于
        dict.get("direction0").attr({"data-filterdata": '3,6',"data-disabled":false})
        limitScoringRubricTable1();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").show()
        isRequire1 = true;
        isRequire2 = false;
    } else if (scoringMethod == 3) {
        text = '评分方法说明：以授信额度为例，将授信额度进行分段并匹配对应区间分值。系统获取评价机构的授信额度，通过授信额<br>度坐落区间估算评价机构得分。<br>'
            + '假设评分细则为：<br> 1.授信额度>=20亿：10分<br>2.授信额度=10亿：8分<br>3.授信额度=5亿：6分<br>4.授信额度<=1亿：0分<br>若评价机构授信额度为15亿，则其得分为9分；'
        $("#remind").html(text)
        html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
        html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> >= </div></td><td width="200px"><div class="layui-input"> 2000000000 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> = </div></td><td width="200px"><div class="layui-input"> 1000000000 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> = </div></td><td width="200px"><div class="layui-input"> 500000000 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 100000000 </div></td><td width="100px;">0</td></tr>'
        html += '</tbody></table>'
        $("#scoringRubricTable1").show();
        //第一行不能选择满分或者等于
        dict.get("direction0").attr({"data-filterdata": '3,6',"data-disabled":false})
        limitScoringRubricTable1();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").show()
        isRequire1 = true;
        isRequire2 = false;
    } else if (scoringMethod == 4) {
        text = '评分方法说明：以授信额度为例，获取评价任务中评价机构的授信额度，将授信额度倒序排列后根据坐落区间取评价机构得分。<br>'
            + '假设评分细则为：<br> 1、1-3名  10分<br> 2、4-6名   8分<br>  3、7-9名   6分<br>  4、其他     4分<br>若评价机构授信额度排名第2，则其得分为10分；'
        $("#remind").html(text)
        html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
        html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 3 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 6 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> > </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">4</td></tr>'
        html += '</tbody></table>'
        $("#description").hide()
        $("#example").show()
        $("#scoringRubricTable1").show();
        //第一行不能选择满分或者等于
        dict.get("direction0").attr({"data-filterdata": '3,6',"data-disabled":false})
        limitScoringRubricTable1();
        $("#scoringRubricTable2").hide();
        isRequire1 = true;
        isRequire2 = false;
    } else if (scoringMethod == 5) {
        text = '评分方法说明：以融资成本为例，获取评价任务中评价机构的融资成本，将融资成本升序排列后根据坐落区间取评价机构得分。<br>'
            + '假设评分细则为：<br> 1、1-3名  10分<br> 2、4-6名   8分<br>  3、7-9名   6分<br>  4、其他     4分<br>若评价机构授信额度排名第2，则其得分为10分；'
        $("#remind").html(text)
        html += '<table class="layui-table layui-table-border scoring-table-sample" style="width: 400px;table-layout: fixed">'
        html += '<tr><td colspan="2">' + bt.lang("credit.indicator.scoringCondition") + '</td><td>' + bt.lang("credit.indicator.grade") + '</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 3 </div></td><td width="100px;">10</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 6 </div></td><td width="100px;">8</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> <= </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">6</td></tr>'
        html += '<tr><td width="100px"><div class="layui-input"> > </div></td><td width="200px"><div class="layui-input"> 9 </div></td><td width="100px;">4</td></tr>'
        html += '</tbody></table>'
        $("#scoringRubricTable1").show();
        dict.get("direction0").attr({"data-filterdata": '3,6',"data-disabled":false})
        limitScoringRubricTable1();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").show()
        isRequire1 = true;
        isRequire2 = false;

    } else if (scoringMethod == 6) {
        $("#example").hide()
        $("#description").show()
        //隐藏table
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").hide();
    } else {

    }

    $("#scoringExample").html(html)
    dict.render()
    form.render()

}

function limitScoringRubricTable1() {
    //判断评分方法
    var scoringMethod = dict.get("scoringMethod").val()
    var trs = $("#scoringRubricTable1 tbody tr");
    $.each(trs, function (index, item) {
        rowNum = $(item).attr("data-row");
        //限制下一行输入的符号
        var filterData = ''
        if (rowNum > 0) {
            //获取上一行的符号
            var preNum = rowNum - 1
            var preScoringSymbol = dict.get("direction" + preNum).val()
            if (scoringMethod != 3) {
                filterData += '3,'
            }
            if (preScoringSymbol == 1) {//上一行选择>
                filterData += '4,6'
            } else if (preScoringSymbol == 2) {//上一行选择>=
                filterData += '5,6'
            } else if (preScoringSymbol == 3) {//上一行选择=
                //判断是否是插值法
                if (scoringMethod == 3 && rowNum >= 2) {//插值法
                    //获取第一行的数据
                    var firstScoringSymbol = dict.get("direction" + 0).val()
                    if (firstScoringSymbol == 1 || firstScoringSymbol == 2) {//第一行选择>|>=
                        filterData += '1,2,6'//后一行只能选择<,<=,=
                    } else if (firstScoringSymbol == 4 || firstScoringSymbol == 5) {//第一行选择<|<=
                        filterData += '4,5,6'//后一行只能选择>,>=,=
                    }
                }
            } else if (preScoringSymbol == 4) {//上一行选择<
                filterData += '1,6'
            } else if (preScoringSymbol == 5) {//上一行选择<=
                filterData += '2,6'
            } else {
                filterData += '6'
            }
            dict.get("direction" + rowNum).attr({"data-filterdata": filterData})
        }
    });
    dict.render()
}

/**
 * 新增评分细则后对评分细则的符号和分数做控制
 * @param row
 */
function afterAdd(row) {
    var scoringMethod = dict.get("scoringMethod").val()
    //限制下一行输入的符号
    var filterData = ''
    //获取上一行的符号
    var preNum = row - 1
    var preScoringSymbol = dict.get("direction" + preNum).val()
    var preScoringStandard = moneyType.get("scoringStandard" + preNum).val()

    if (scoringMethod != 3) {
        filterData += '3,'
    }
    if (preScoringSymbol == 1) {//上一行选择>
        filterData += '4,6'
    } else if (preScoringSymbol == 2) {//上一行选择>=
        filterData += '5,6'
    } else if (preScoringSymbol == 3) {//上一行选择=
        //判断是否是插值法
        if (scoringMethod == 3 && row >= 2) {//插值法
            //获取第一行的数据
            var firstScoringSymbol = dict.get("direction" + 0).val()
            if (firstScoringSymbol == 1 || firstScoringSymbol == 2) {//第一行选择>|>=
                filterData += '1,2,6'//后一行只能选择<,<=,=
            } else if (firstScoringSymbol == 4 || firstScoringSymbol == 5) {//第一行选择<|<=
                filterData += '4,5,6'//后一行只能选择>,>=,=
            }
        }
    } else if (preScoringSymbol == 4) {//上一行选择<
        filterData += '1,6'
    } else if (preScoringSymbol == 5) {//上一行选择<=
        filterData += '2,6'
    } else {
        filterData += '6'
    }
    //让上一行可编辑
    // $(".direciton-row"+(row-1)).attr("disabled","true");
    // moneyType.get("scoringStandard"+(row-1)).attr("readonly","true")
    // createDireciton($('.direciton-row'+rowStr),rowStr,filterData)
    dict.get("direction" + row).attr({"data-filterdata": filterData})
    //隐藏上一行的按钮
    $('[name = "add' + (row - 1) + '"]').hide();
    $('[name = "del' + (row - 1) + '"]').hide();
    dict.render()
    moneyType.init()

}

/**
 * 指标性质回调
 * @param data
 */
function indexPropertyCallback(data) {
    afterIndexProperty(data.value);
}

/**
 * 判断指标性质后处理
 * @param value
 */
function afterIndexProperty(value) {
    //定量
    if (value == 1) {
        dict.get("isAutoScoring").attr({"data-required": true});
        $(".isAutoScoring").show();
        dict.get("scoringMethod").attr({"data-required": true});
        $(".scoringMethod").show();
        dict.get("dataSource").attr({"data-required": true});
        $(".dataSource").show();
        $("#description").hide()
        $("#example").show()
        replaceScoringRubric(dict.get("scoringMethod").val())
    } else if (value == 2) {//定性
        dict.get("isAutoScoring").attr({
            "data-required": false
        });
        $(".isAutoScoring").hide();
        dict.get("scoringMethod").attr({"data-required": false});
        $(".scoringMethod").hide();
        dict.get("dataSource").attr({"data-required": false});
        $("#dataSourceName").removeAttr('lay-verify')
        // $("#dataSourceName").val("")
        // $("#dataSourceId").val("")
        // $("#dataSourceOrigin").val("")
        // $("#dataSourceCode").val("")
        $("#scoringR").show()
        $(".dataSource").hide();
        $("#description").show()
        $("#example").hide()
        //隐藏table
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").hide();
    } else {
        $("#scoringRubricTable1").hide();
        $("#scoringRubricTable2").hide();
        $("#description").hide()
        $("#example").hide()
    }
}

/**
 * 数据源回调
 */
function dataSourceCallback(data) {
    $("#dataSourceName").val(data.dataSourceName)
    $("#dataSourceId").val(data.id)
    $("#dataSourceOrigin").val(data.dataSourceOrigin)
    $("#dataSourceCode").val(data.dataSourceCode)
}

function getFormFields(formId) {
    var field = {};
    var fieldElem = $(formId).find('input,select,textarea') //获取所有表单域
    var multiCheckbox = [];//用于存储是否为多个checkbox
    $.each(fieldElem, function (_, item) {

        if (!item.name) return;
        if (/^checkbox|radio$/.test(item.type)) {
            if ("radio" === item.type && !item.checked) {
                return;
            }
            if ("checkbox" === item.type) {
                var laySkin = item.getAttribute("lay-skin");
                if (laySkin !== "primary" && !item.checked) {
                    return;
                }
                if (!field[item.name]) {
                    field[item.name] = "";
                }
                if (!item.checked) {
                    return;
                }
                //获取form表单中checkbox数量是否为多个
                multiCheckbox[item.name] = $('[name="' + item.name + '"]').length > 1;
                if (field[item.name] === "") {
                    if (multiCheckbox[item.name]) {
                        field[item.name] = [];
                    }
                }
                if (multiCheckbox[item.name]) {
                    if ($(item).data('extra') !== 'all') { // 如果 复选框中有 data-extra ＝ 'all'时，说明这个值是前台额外加的
                        field[item.name].push(item.value);
                    }
                } else {
                    field[item.name] = item.value;
                }
                return;
            }
        }
        // field[item.name] = item.value;
        field[item.name] = $.trim(item.value); //删除数据的前后空格  liuyj 2018-05-25
    });
    return field
}


function clearScoringRubricTable(tableId) {
    //只保留一行评分细则
    bt.each($("#" + tableId).find('tr'), function (i, tr) {
        if (i > 1) {
            $(tr).remove();
        } else if (i == 1) {
            $('[name = "add0"]').show();
        }
    })

    bt.each($("#" + tableId).find('.grade'), function (i, grade) {
        $(this).val(bt.money.format(0)).siblings('input').val(bt.money.format(0))
    })
    bt.each($("#" + tableId).find('.direction'), function (i, direction) {
        $(this).find('select').val('');
        $(this).find('input').val(' ');
        $(this).find('.layui-this').removeClass('layui-this');
    })
    bt.each($("#" + tableId).find('.scoringStandard'), function (i, scoringStandard) {
        $(this).val(bt.money.format(0)).siblings('input').val(bt.money.format(0))
    })
}
