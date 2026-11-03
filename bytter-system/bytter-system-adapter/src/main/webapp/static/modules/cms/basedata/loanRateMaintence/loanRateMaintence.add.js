var rowMR=0,dict;
bt.use(['form','rateType','dateType','moneyType','bttable','selectType','dicts','select'], function(){
	$ = bt.jquery,form = bt.form();
	selectType = bt.selectType.init();
    dict=bt.dicts.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    //新增行事件
    $("#add").click(function(){
        var baseSign = $("#isBaseLoanRate").val();
        var html='<tr><input type="hidden" id="rowSign'+rowMR+'" value="'+rowMR+'">';
        html +='<td><div class="layui-inline"><div class="bt-biz-selectType" data-name="mainList['+ rowMR +'].creditPreionName"  data-method="post" data-url="' + basePath +dataUrl+'" '
        +'data-valuefield="code" data-textfield="periodName" id="'+rowMR+'code" data-biz-name="'+rowMR+'code" data-search="false"  data-link="true"  data-callback="bt.setData"></div>' +
            '<input type="hidden" name="mainList['+ rowMR +'].creditPeriodId" id="creditPeriodId'+rowMR+'"></td>';
        html += '<td><input type="text" class="layui-input"  name="loanClass" id="loanClass'+rowMR+'" disabled="disabled"></td>';
        html += '<td ><input name="mainList['+ rowMR +'].timeOfTakeEffect" lay-verify="required" class="layui-input bt-biz-dateType" data-type="date" type="text"></td>';
        var code = $("#code").val();
        if(baseSign == "1"){
            html += '<td><div class="layui-inline"><input type="text" data-name="mainList[' + rowMR + '].rate" data-moneyendid="mainList' + rowMR + '_rateUpLimit" data-id="mainList' + rowMR + '_rate" lay-verify="required" data-max="100" data-supermin="0" class="layui-input bt-biz-rateType" ></div></td>';
            if(code == 'lpr'){
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateUpLimit" data-id="mainList'+ rowMR +'_rateUpLimit" lay-verify="required" data-max="10000" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">BP</span></div></div></td>'
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateDownLimit" data-moneyendid="mainList'+ rowMR +'_rateUpLimit" data-id="mainList'+ rowMR +'_rateDownLimit" lay-verify="required" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">BP</span></div></div></td>'
            }else {
                /*html += '<td><input type="text" data-name="mainList[' + rowMR + '].rateUpLimit" data-id="mainList' + rowMR + '_rateUpLimit" lay-verify="required" data-max="100" data-min="-100" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" ></td>';
                html += '<td><input type="text" data-name="mainList[' + rowMR + '].rateDownLimit" data-moneyendid="mainList' + rowMR + '_rateUpLimit" data-id="mainList' + rowMR + '_rateDownLimit" lay-verify="required" data-min="-100" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';*/
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateUpLimit" data-id="mainList'+ rowMR +'_rateUpLimit" lay-verify="required" data-max="10000" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">%</span></div></div></td>'
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateDownLimit" data-moneyendid="mainList'+ rowMR +'_rateUpLimit" data-id="mainList'+ rowMR +'_rateDownLimit" lay-verify="required" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">%</span></div></div></td>'
            }
        }else{
           html += '<td><div class="layui-inline"><input type="text" data-name="mainList[' + rowMR + '].rate" data-moneyendid="mainList' + rowMR + '_rateUpLimit" data-id="mainList' + rowMR + '_rate" lay-verify="required" data-max="100" data-supermin="0" class="layui-input bt-biz-rateType" ></div></td>';
            if(code == 'lpr'){
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateUpLimit" data-id="mainList'+ rowMR +'_rateUpLimit" lay-verify="required" data-max="10000" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">BP</span></div></div></td>'
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateDownLimit" data-moneyendid="mainList'+ rowMR +'_rateUpLimit" data-id="mainList'+ rowMR +'_rateDownLimit" lay-verify="required" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">BP</span></div></div></td>'
            }else {
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateUpLimit" data-id="mainList'+ rowMR +'_rateUpLimit" lay-verify="required" data-max="10000" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">%</span></div></div></td>'
                html += '<td name="numUnit"><div class="layui-inline input-bp"><div class="pl rateWrap" style="width:212px"><input type="text" data-name="mainList['+ rowMR +'].rateDownLimit" data-moneyendid="mainList'+ rowMR +'_rateUpLimit" data-id="mainList'+ rowMR +'_rateDownLimit" lay-verify="required" data-min="-10000" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" >';
                html += '<span name="percentsymbol" class="inputMark">%</span></div></div></td>'
            }
        }
        html+='<td>&nbsp;&nbsp;&nbsp;&nbsp;\t'
            + '<button data-type="del" type="button" class="layui-btn layui-btn-mini">'+bt.lang("common.button.delete")+'</button>'
            + '</td>';
        html+='</tr>';
        $("#editTable tbody:first").append(html);
        initSelectCode(rowMR);
        bt.initDocument();
        rowMR++;
        return false;
    });

    var initSelectCode=function(row){
        bt.dicts.init();
        bt.moneyType.init();
        bt.dateType.init();
        bt.setPeriodDisable(row);
        bt.selectType.init();
    }

    //新增行设置已勾选的周期不可选择
    bt.setPeriodDisable = function(rowIndex){
        var doms = $('[data-link="true"]');
        var periodCode = "";
        bt.each(doms, function(i,item){
            var othat = $(this);
            var dataObj = $(othat).find("option:selected").val();
            if(!bt.empty(dataObj)){
                periodCode += dataObj + ",";
            }
        })
        periodCode = bt.jquery.trim(periodCode);
        if(periodCode.length > 1){
            periodCode = periodCode.substr(0, periodCode.length - 1);
            $("#"+rowIndex + "code").attr("data-itemdisabled", periodCode);
            form.render("select");
        }
    }

	// /**初始化编辑表格中的控件*/
	bt.initDocument = function (){
		bt.rateType.init();
		bt.dateType.init();
		bt.moneyType.init();
	}

	//监听提交
    form.on('submit(submit)', function(data){
        var action = "/cms/loanrate/maintence/saveBaseRate";

        // 1. 先把所有 mainList[数字].xxx 提取出来
        var itemMap = {}; // key: 行号, value: {属性名:值}

        for (var key in data.field) {
            // 正则精准匹配：mainList[数字].属性名  （你的格式）
            var reg = /^mainList\[(\d+)\]\.(.+)$/;
            var match = key.match(reg);

            if (match) {
                var index = match[1];      // 行号 0、4...
                var field = match[2];     // 属性名 creditPreionName、rate...
                var value = data.field[key];

                if (!itemMap[index]) {
                    itemMap[index] = {};
                }
                itemMap[index][field] = value; // 把同一行的所有字段组装起来
                delete data.field[key]; // 删除旧的断裂key
            }
        }

        // 2. 把断裂的行号 0、4... 重新变成连续 0、1、2...
        var index = 0;
        for (var key in itemMap) {
            var item = itemMap[key];
            // 重新塞回 data.field，变成连续下标
            for (var field in item) {
                data.field["mainList[" + index + "]." + field] = item[field];
            }
            index++;
        }
        // ==================================================================

        $.post(basePath + action,data.field,function(result){
        	parent.layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
            	bt.navtab.closeCurrent();
            }
        },'JSON');
        return false;
    });

    //根据借款周期设置初始化数据
    bt.setData = function(typeVal, elem, bv, wrapObj) {
        //异步获取借款周期信息，如果只有一种借款周期默认选择第一条
        var dataId = $(wrapObj).attr("id");
        var Index = dataId.indexOf("code");
        var rowIndex = dataId.substr(0, Index);
        var code = typeVal.code;

        var loanClassName=bt.dict('cms.LoanClass',typeVal.loanclass);//贷款期限类型
        $("#loanClass" + rowIndex).val(loanClassName);

        $("#creditPeriodId"+ rowIndex).val(typeVal.id);
        bt.setPeriodCode(typeVal,elem,bv);
        bt.dicts.init();
    }

    //设置其他借款周期不可选
    bt.setPeriodCode = function(data,elem,bv){
        var Othis = elem.othis.parent('[data-link="true"]');
        var v = elem.value;
        var doms = $('[data-link="true"]');
        bt.each(doms,function(){
            var othat = $(this);
            if(!othat.is(Othis)){
                othat.find('select option[value="'+v+'"]').prop("disabled",true);
                othat.find('select option[value="'+bv+'"]').prop("disabled",false);
            }
        })
        form.render("select");
    }

    //初始化删除按钮
    var initEditTable=function(){
        //生成列表
        bt.bttable.init({
            scrollX:false,
            scrollY:false,
            clickTrSelected : false,
            elem: "#editTable",
            buttons: [
            ],
            columns:[],
            delRowCallback:function(data,$tr){
                //删除后更新其他行的借款周期品种选择
                $tr.remove();
                rowMR--;
                form.render("select");
            },
        });
    }
    initEditTable();

});



function closeWindow(){
	bt.navtab.closeCurrent();
}

function changeTitle(data) {
    var code = data.code;
    if(code == "lpr"){
        $("#upName").text(bt.lang("table.tbLoanRateMaintence.rateUpLimitPoints"));
        $("#downName").text(bt.lang("table.tbLoanRateMaintence.rateDownLimitPoints"));
        $("#rateName").text(bt.lang("table.depositRate.baseRate"));
        $("#code").val("lpr");
        $.each($("td[name = 'numUnit']"), function (index, item) {
            $(this).find("span[name='percentsymbol']").html("BP");
        });
    }else {
        $("#upName").text(bt.lang("table.tbLoanRateMaintence.rateUpLimitProportion"));
        $("#downName").text(bt.lang("table.tbLoanRateMaintence.rateDownLimitProportion"));
        $("#rateName").text(bt.lang("table.tbLoanRateMaintence.rate"));
        $("#code").val("");
        $.each($("td[name = 'numUnit']"), function (index, item) {
            $(this).find("span[name='percentsymbol']").html("%");
        });
    }
}
