var select,dict;
bt.use(['form','dicts','selectType','bttable'], function(){
    $ = bt.jquery,form = bt.form();
    /**
     * 资金池回调
     * @param data
     */
    poolCallBack = function (data) {
        // 重新设置资金池的子单位
        var poolChildUrl = basePath + "/foreign/fexcorppool/getFexCorpPoolChilds?poolId=" + data.id;
        bt.selectType.get("bankAcc").attr("data-url", poolChildUrl);
        $("#bankName").html('');
        $("#englishCode").html('');
        $("#poolNature").html(bt.dict("fexCorpPool.poolNature", data.poolnature));
        $("#rmk").val(data.rmk);
    }

    /**
     * 选择账户回调
     * @param data
     */
    accountCallBack = function (data) {
        if(data.id){
            $("#bankAccId").val(data.id);
            $("#bankName").html(data.bankname);
            $("#englishCode").html(data.englishcode);
        }else{
            $("#bankAccId").val('');
            $("#bankName").html('');
            $("#englishCode").html('');
        }
    };

    /**
     * 借入外债交易回调函数
     * @param data
     */
    bt.tranTypeJ = function(data){
        var ins = $(data.othis).parents('tr').attr("row");
        var productUrl = basePath + "/foreign/fexproduct/listProduct/" + data.value;
        bt.selectType.get("tranTypeJDtl["+ins+"].proId").attr("data-url",productUrl);
    }

    /**
     * 对外放款交易回调函数
     * @param data
     */
    bt.tranTypeW = function(data){
        var ins = $(data.othis).parents('tr').attr("row");
        var productUrl = basePath + "/foreign/fexproduct/listProduct/" + data.value;
        bt.selectType.get("tranTypeWDtl["+ins+"].proId").attr("data-url",productUrl);
    }

    /**
     * 重置回调
     */
    resetCallBack = function(){
        $("#poolNature").html('');
        $("#bankName").html('');
        $("#englishCode").html('');
    }

    dict = bt.dicts.init();
    select = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/foreign/fexcorppooltran/save";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.navtab.refreshParent();
                bt.navtab.refreshCurrent();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });


    //生成列表
    var rowMR = 1;
    bt.bttable.init({
        elem: "#dataTable1",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                var html = '<tr row=' + rowMR + '>';
                html += '<td align="center"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-name="tranTypeJDtl['+rowMR+'].tranType" data-biz-name="tranTypeJDtl['+rowMR+'].tranType" data-group="fexCorpPoolTran.tranType" data-required="true" data-callback="bt.tranTypeJ" data-displayData="3"></div></div></td>';
                html += '<td align="center"><div class="layui-inline"><div class="bt-biz-selectType" data-url="" data-name="tranTypeJDtl['+rowMR+'].proId" data-biz-name="tranTypeJDtl['+rowMR+'].proId" data-valuefield="id" data-textfield="proName" data-search="true" data-required="true"></div></div></td>';
                html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">'+bt.lang("common.button.delete")+'</a></td>';
                html += '</tr>';
                $("#dataTable1").children("tbody").append(html);
                bt.dicts.init();
                bt.selectType.init();
                form.render('select');
                rowMR++;
            }
        }],
        tableInitCallback:function(){
            bt.dicts.init();
            bt.selectType.init();
            form.render('select');
        },
        columns: [],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });


    //生成列表
    var rowNum = 1;
    bt.bttable.init({
        elem: "#dataTable2",
        buttons: [{
            text: '<i class="layui-icon">&#xe642;</i>' + bt.lang('common.addTr'),
            action: function ($button) {
                var html = '<tr row=' + rowNum + '>';
                html += '<td align="center"><div class="layui-inline"><div class="bt-biz-dict" data-type="select" data-name="tranTypeWDtl['+rowNum+'].tranType" data-biz-name="tranTypeWDtl['+rowNum+'].tranType" data-group="fexCorpPoolTran.tranType" data-required="true" data-callback="bt.tranTypeW" data-displayData="3"></div></div></td>';
                html += '<td align="center"><div class="layui-inline"><div class="bt-biz-selectType" data-url="" data-name="tranTypeWDtl['+rowNum+'].proId" data-biz-name="tranTypeWDtl['+rowNum+'].proId" data-valuefield="id" data-textfield="proName" data-search="true" data-required="true"></div></div></td>';
                html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">'+bt.lang("common.button.delete")+'</a></td>';
                html += '</tr>';
                $("#dataTable2").children("tbody").append(html);
                bt.dicts.init();
                bt.selectType.init();
                form.render();
                rowNum++;
            }
        }],
        tableInitCallback:function(){
            bt.dicts.init();
            bt.selectType.init();
            form.render();
        },
        columns: [],
        delRowCallback: function (data, $tr) {
            $tr.remove();
        }
    });

});

/**
 * 主账号回调
 * @param data
 * @param dom
 */
accCallBack = function (data,dom) {
    var row = dom.othis.parents("tr").attr("row");
    $("#corpId"+row).val(data.corpid);
    $("#curId"+row).val(data.btbankacccurdtolist[0].curId);
    $("#showCorp"+row).html($("#corpName").val());
    $("#showCur"+row).html($("#englishCode").val());
}

/**
 * 查看账号
 * @param row
 * @returns {boolean}
 */
function viewPoolAcc(row) {
    var accId = $("#accId"+row).val();
    var curId = $("#curId"+row).val();
    if (bt.empty(accId) || bt.empty(curId)) {
        layer.alert(bt.lang("table.btwarnstrategy.accountselect.errorMessage"), {icon: 7});
        return false;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath +"/bankacc/btbankaccquery/view?id=" + accId + "&curId=" + curId
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