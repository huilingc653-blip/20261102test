var rowNum=$("#rowIndex").val();
var dicts,moneyType;
bt.use(['form','rateType','dateType','moneyType','bttable','selectType','dicts','select'], function(){
	$ = bt.jquery,form = bt.form();


    /**
     * 天数回调函数
     * @param data
     * @param elem
     */
	bt.changeDays = function(data,elem){
        var num = $(elem).parents('tr').attr("data-row");
        createFormulaInfo(num);
    }

    /**
     * 数据字典回调函数
     * @param rowNum
     */
    bt.createFormula = function(data){
        var ins = $(data.othis).parents('tr').attr("data-row");
        createFormulaInfo(ins);
    }
	moneyType = bt.moneyType.init();
    bt.selectType.init();
    dicts = bt.dicts.init();

    // 监听提交
    form.on('submit(submit)', function(data){
        bt.loading.show();//显示加载中
        var action = "/report/remainTerm/submit";
        $.post(basePath + action,data.field,function(result){
            bt.loading.hide(); //关闭加载中
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                if(!bt.navtab){
                    parent.layer?parent.layer.closeAll():layer.closeAll();
                } else {
                    bt.navtab.closeCurrent();
                    bt.navtab.refreshParentTable();
                }
            }
        },'JSON');
        return false;
    });

    // 新增行
    $("#add").click(function(){
        var html = '<tr data-row="'+rowNum+'"><input type="hidden" id="rowSign'+rowNum+'" value="'+rowNum+'">';
        html += '<td class="center">'+bt.dict('remainTerm.field','2')+'<div class="layui-inline"><input type="hidden" name="remainTermDtl['+ rowNum +'].field1" ' +
            'id="'+ rowNum +'field1" value="2"/></div></td>';
        html += '<td class="center">'+bt.dict('remainTerm.symbol','2')+'<div class="layui-inline"><input type="hidden" name="remainTermDtl['+ rowNum +'].symbol" ' +
            'id="'+ rowNum +'symbol" value="2"/></div></td>';
        html += '<td class="center">'+bt.dict('remainTerm.field','1')+'<div class="layui-inline"><input type="hidden" name="remainTermDtl['+ rowNum +'].field2" ' +
            'id="'+ rowNum +'field1" value="1"/></div></td>';
        html += '<td class="center"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-name="remainTermDtl['+ rowNum +'].conditions" ' +
            'data-biz-name="remainTermDtl['+ rowNum +'].conditions" data-group="remainTerm.condition" data-required="true" data-callback="bt.createFormula"></div></div></td>';
        html += '<td class="center"><div class="layui-inline"><input type="text" data-name="remainTermDtl['+ rowNum +'].days" id="'+ rowNum +'days" data-biz-name="remainTermDtl['+ rowNum +'].days"' +
            'class="layui-input bt-biz-moneyType" lay-verify="required" data-min="0" data-decimal="0" data-callback="bt.changeDays" /></div></td>';
        html += '<td class="center"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-required="true" data-name="remainTermDtl['+ rowNum +'].unit" ' +
            'data-biz-name="remainTermDtl['+ rowNum +'].unit" data-group="remainTerm.unit" data-required="true"  data-callback="bt.createFormula"></div></div></td>';
        html += '<td class="center"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-name="remainTermDtl['+ rowNum +'].logicalRelationship" ' +
            'data-biz-name="remainTermDtl['+ rowNum +'].logicalRelationship" data-group="remainTerm.logicalRelationship"  data-callback="bt.createFormula"></div></div></td>';
        html += '<td class="center"><button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button></td>';
        html += '<td style="display: none"><input type="hidden" readonly="true" name="remainTermDtl['+ rowNum +'].formula" id="'+ rowNum +'formula"/></td>>'; // 保存公式
        html += '<td style="display: none"><input type="hidden" readonly="true" name="remainTermDtl['+ rowNum +'].sort" id="'+ rowNum +'sort"/></td>>';  // 保存顺序
        html += '</tr>';
        $("#editTable tbody:last").append(html);
        renderForm();
        setDisable();
        sortTr();
        rowNum++;
        return false;
    });

    /**
     * 渲染表单
     */
    var renderForm=function(){
        bt.dicts.init();
        bt.moneyType.init();
        bt.dateType.init();
        bt.selectType.init();
    }

    /**
     * 初始化列表
     */
    var initEditTable=function(){
        bt.bttable.init({
            scrollX:false,
            scrollY:false,
            elem: "#editTable",
            fixedColumns: {
                rightColumns:1
            },
            buttons: [
            ],
            columns:[],
            delRowCallback:function(data,$tr){
                $tr.remove();
                sortTr();
                setDisable();
                // 创建公式
                createFormulas();
                form.render("select");
            },
        });
    }
    initEditTable();
    setDisable();
    /**
     * 创建公式
     * @param index
     */
    function createFormulaInfo(index) {
        var rowIndex = index;
        // var field1 = getSelectData('remainTermDtl['+ rowIndex +'].field1','2').text;
        var field1 = bt.dict('remainTerm.field','2');
        var symbol = bt.dict('remainTerm.symbol','2');
        //var symbol = getSelectData('remainTermDtl['+ rowIndex +'].symbol',dicts.get('remainTermDtl['+ rowIndex +'].symbol').val()).text;
        //var field2 = getSelectData('remainTermDtl['+ rowIndex +'].field2','1').text;
        var field2 = bt.dict('remainTerm.field','1');
        //var condition = getSelectData('remainTermDtl['+ rowIndex +'].condition',dicts.get('remainTermDtl['+ rowIndex +'].condition').val()).text;
        var condition = bt.dict('remainTerm.condition',dicts.get('remainTermDtl['+ rowIndex +'].conditions').val());
        var days = $('#'+rowIndex+'days').val();
        //var unit = getSelectData('remainTermDtl['+ rowIndex +'].unit',dicts.get('remainTermDtl['+ rowIndex +'].unit').val()).text;
        var unit = bt.dict('remainTerm.unit',dicts.get('remainTermDtl['+ rowIndex +'].unit').val());
        if (!bt.empty(field1) && !bt.empty(symbol) && !bt.empty(field2)
            && !bt.empty(condition) && !bt.empty(days) && !bt.empty(unit)) {
            var formula = "" + field1 + symbol + field2 + replaceCondition(condition) + days + unit;
            $("#"+rowIndex+"formula").val(formula);
            createFormulas();
        }
    }
});

/**
 * 创建公式
 */
function createFormulas(){
    var formulas = '';
    $("#editTable").find("tbody tr").each(function(i){
        var index = $(this).prev().attr("data-row");
        $(this).find("td").each(function (j) {
            if (j==8) {
                if (i>=1 && i < ($("#editTable").find("tbody tr").length)) {
                    //var relation = getSelectData('remainTermDtl['+ index +'].logicalRelationship',dicts.get('remainTermDtl['+ index +'].logicalRelationship').val()).text;
                    var relation = bt.dict('remainTerm.logicalRelationship',dicts.get('remainTermDtl['+ index +'].logicalRelationship').val());
                    if (!bt.empty(relation) && !bt.empty(formulas)) {
                        formulas =  formulas + " "+relation + " "+$(this).find("input").val();
                    }
                }else {
                    formulas += $(this).find("input").val();
                }
            }
        });
    });
    $("#formulas").html(formulas);
}

/**
 * 排序
 */
function sortTr() {
    $("#editTable").find("tbody tr").each(function(i){
        var index = $(this).attr("data-row");
        $("#"+index+"sort").val(i)
    });
}

/**
 * 判断字符串是否为空
 * @param ms
 * @returns {boolean}
 */
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}


/**
 * 获取数据字典中的name
 * @param bizName
 * @param value
 * @returns {*}
 */
function getSelectData(bizName,value) {
    var data = window.biz['Select'][bizName].data;
    for(var i = 0;i<data.length;i++){
        if(data[i].value == value){
            return data[i]
        }
    }
}

function setDisable() {
    $("#editTable").find("tbody tr").each(function(j){
        var index = $(this).attr("data-row");
        var logicalRelationship = dicts.get('remainTermDtl['+ index +'].logicalRelationship').val();
        if (j == ($("#editTable").find("tbody tr").length -1)) {
            dicts.get('remainTermDtl['+ index +'].logicalRelationship').val("");
            dicts.get('remainTermDtl['+ index +'].logicalRelationship').attr({
                'data-disabled':true,
                'data-required':false
            });
        }
        else {
            dicts.get('remainTermDtl['+ index +'].logicalRelationship').attr({
                'data-disabled':false,
                'data-defaultvalue':logicalRelationship,
                'data-required':true
            });
        }
    });
}

/**
 * 关闭页面
 */
function closeWindow(){
	bt.navtab.closeCurrent();
}

/**
 * 替换条件，适配中文，页面有特殊字符过滤
 * @param condition
 * @returns {string}
 */
function replaceCondition(condition) {
    if ('>' === condition) {
        return '大于';
    } else if ('<' === condition) {
        return '小于';
    } else if ('=' === condition) {
        return '等于';
    } else if ('<=' === condition) {
        return '小于等于';
    } else if ('>=' === condition) {
        return '大于等于';
    } else if ('≠' === condition || '!=' === condition) {
        return '不等于';
    }
}
