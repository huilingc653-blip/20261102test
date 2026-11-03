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
        bt.selectType.get("bankAcc").attr({"data-defaultvalue":"","data-url":poolChildUrl});
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
        select.get("bankAcc").attr({"data-url": basePath + "/foreign/fexcorppool/getFexCorpPoolChilds?poolId="+$("#poolId").val(),"data-defaultvalue":$("#bankaccId").val()});
    }

    dict = bt.dicts.init();
    select = bt.selectType.init();
    //监听提交
    form.on('submit(submit)', function (data) {
        var action = "/foreign/fexcorppooltran/modify";
        $.post(basePath + action, data.field, function (result) {
            if(result.success){
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                bt.navtab.closeCurrent();
            }
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        }, 'JSON');
        return false;
    });

    //生成列表
    var rowMR = 0;
    bt.bttable.init({
        elem: "#dataTable1",
        url: basePath + "/foreign/fexcorppooltran/tranInfo?poolId=" + $("#poolId").val()+"&bankaccId="+$("#bankaccId").val()+"&tranSign=0&curId="+$("#curId").val(),
        checkbox: false,//带checkboxs
        buttons: [//表格上面按钮功能
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    var html = '<tr row=' + rowMR + '>';
                    html += '<td align="center"><div class="layui-inline"><input type="hidden" id="id'+rowMR+'" name=tranTypeJDtl['+rowMR+'].id  autocomplete="off" class="layui-input" value=""><div class="bt-biz-dict" data-type="select" data-name="tranTypeJDtl['+rowMR+'].tranType" data-biz-name="tranTypeJDtl['+rowMR+'].tranType" data-group="fexCorpPoolTran.tranType" data-required="true" data-callback="bt.tranTypeJ" data-displayData="3"></div></div></td>';
                    html += '<td align="center"><div class="layui-inline"><div class="bt-biz-selectType" data-url="" data-name="tranTypeJDtl['+rowMR+'].proId" data-biz-name="tranTypeJDtl['+rowMR+'].proId" data-valuefield="id" data-textfield="proName" data-search="true" data-required="true"></div></div></td>';
                    html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">'+bt.lang("common.button.delete")+'</a></td>';
                    html += '</tr>';
                    $("#dataTable1").children("tbody").append(html);
                    bt.dicts.init();
                    bt.selectType.init();
                    form.render('select');
                    rowMR++;
                }
            }
        ],
        // btInitComplete:function(setting,json){
        tableInitCallback: function () {
            bt.dicts.init();
            bt.selectType.init();
            form.render('select');
        },

        columns: [
            {
                "data": "tranType", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<div class="layui-inline"><input type="hidden" id="mid'+rowMR+'" name=tranTypeJDtl['+rowMR+'].id  autocomplete="off" class="layui-input" value="'+data.id+'"><div class="bt-biz-dict" data-type="select" data-name="tranTypeJDtl['+rowMR+'].tranType" data-biz-name="tranTypeJDtl['+rowMR+'].tranType" data-group="fexCorpPoolTran.tranType" data-required="true" data-defaultvalue="'+data.tranType+'" data-callback="bt.tranTypeJ" data-displayData="3"></div></div>';
                    return html;
                }
            }, {
                "data": "proId", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<div class="layui-inline"><div class="bt-biz-selectType" data-url='+basePath + '/foreign/fexproduct/listProduct/'+data.tranType+' data-name="tranTypeJDtl['+rowMR+'].proId" data-biz-name="tranTypeJDtl['+rowMR+'].proId" data-valuefield="id" data-textfield="proName" data-search="true" data-defaultvalue="'+data.proId+'" data-extraData="false" data-required="true"></div></div>';
                    return html;
                }
            },{
                "data": "", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
                    rowMR++;
                    return html
                }
            }
        ],
        delRowCallback: function (data, $tr) {
            var id = $("#mid"+$tr.attr("row")).val();
            if(bt.empty(id)){
                $tr.remove();
            }else{
                delData(id);
            }
        }
    });


    //生成列表
    var rowNum = 0;
    bt.bttable.init({
        elem: "#dataTable2",
        url:  basePath + "/foreign/fexcorppooltran/tranInfo?poolId=" + $("#poolId").val()+"&bankaccId="+$("#bankaccId").val()+"&tranSign=1&curId="+$("#curId").val(),
        checkbox: false,//带checkboxs
        buttons: [//表格上面按钮功能
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    var html = '<tr row=' + rowNum + '>';
                    html += '<td align="center"><div class="layui-inline"><input type="hidden" id="nid'+rowNum+'" name=tranTypeWDtl['+rowNum+'].id  autocomplete="off" class="layui-input" value=""><div class="bt-biz-dict" data-type="select" data-name="tranTypeWDtl['+rowNum+'].tranType" data-biz-name="tranTypeWDtl['+rowNum+'].tranType" data-group="fexCorpPoolTran.tranType" data-required="true" data-callback="bt.tranTypeW" data-displayData="3"></div></div></td>';
                    html += '<td align="center"><div class="layui-inline"><div class="bt-biz-selectType" data-url="" data-name="tranTypeWDtl['+rowNum+'].proId" data-biz-name="tranTypeWDtl['+rowNum+'].proId" data-valuefield="id" data-textfield="proName" data-search="true" data-required="true"></div></div></td>';
                    html += '<td align="center"><a href="javascript:void(0)" data-type="del" class="tableElema">'+bt.lang("common.button.delete")+'</a></td>';
                    html += '</tr>';
                    $("#dataTable2").children("tbody").append(html);
                    bt.dicts.init();
                    bt.selectType.init();
                    form.render();
                    rowNum++;
                }
            }
        ],
        // btInitComplete:function(setting,json){
        tableInitCallback: function () {
            bt.dicts.init();
            bt.selectType.init();
            form.render();
        },

        columns: [
            {
                "data": "tranType", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<div class="layui-inline"><input type="hidden" id="id'+rowNum+'" name="tranTypeWDtl['+rowNum+'].id"  autocomplete="off" class="layui-input" value="'+data.id+'"><div class="bt-biz-dict" data-type="select" data-name="tranTypeWDtl['+rowNum+'].tranType" data-biz-name="tranTypeWDtl['+rowNum+'].tranType" data-group="fexCorpPoolTran.tranType" data-required="true" data-defaultvalue="'+data.tranType+'" data-callback="bt.tranTypeW" data-displayData="3"></div></div>';
                    return html;
                }
            }, {
                "data": "proId", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<div class="layui-inline"><div class="bt-biz-selectType" data-url='+basePath + '/foreign/fexproduct/listProduct/'+data.tranType+' data-name="tranTypeWDtl['+rowNum+'].proId" data-biz-name="tranTypeWDtl['+rowNum+'].proId" data-valuefield="id" data-textfield="proName" data-search="true" data-defaultvalue="'+data.proId+'" data-extraData="false" data-required="true"></div></div>';
                    return html;
                }
            },{
                "data": "", className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a href="javascript:void(0)" data-type="del" class="tableElema">' + bt.lang("common.button.delete") + '</a>';
                    rowNum++;
                    return html
                }
            }
        ],
        delRowCallback: function (data, $tr) {
            var id = $("#nid"+$tr.attr("row")).val();
            if(bt.empty(id)){
                $tr.remove();
            }else{
                delData(id);
            }
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
 * 删除数据
 */
function delData(id) {
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fexcorppooltran/delete/" + id , function (result) {
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
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