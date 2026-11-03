var dateTypes,dicts,selectTypes,moneyTypes,guaPledgeInfoInit,parameters,moneyFun;
var dataInfo="";
var totalMoney=0.0;
var pIds="";
var pledgeNos="";//抵质押物编号
var pledgeMoney="";
bt.use(['form','jquery','dateType','selectType','dicts','moneyType','selectType','guaPledgeInfo'],function(){
    $ = bt.jquery,form = bt.form();
    dateTypes=bt.dateType.init();
    dicts = bt.dicts.init();//字典
    selectTypes=bt.selectType.init();
    moneyTypes = bt.moneyType.init();//金额
    moneyFun = bt.moneyType.getFun();
    parameters=new Object();
    guaPledgeInfoInit=bt.guaPledgeInfo.init();

    /**判断字符串是否为空*/
    bt.empty = function(ms){
        if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
            return true;
        }
        return false;
    }

    form.on('submit(sure)', function(data){
        var relateFlag=$("#relateFlag").val();
        if(!bt.empty(relateFlag)&&relateFlag=="true"){//外部模块挂接标识
            $('#infoTable tbody tr').each(function(i){
                var currentId = $(this).attr('id');
                if(currentId!=undefined){
                    var money="";
                    pIds+=currentId+",";
                    $(this).children('td').each(function(j){
                        if(j==1){//抵质押物编号列
                            pledgeNos+=$(this).text()+",";
                        }
                        if(j==8){//占用额度列
                            money=$(this).find(".money").val();
                            pledgeMoney+=moneyFun.toNumber(money)+";";
                            totalMoney+= moneyFun.toNumber(money);
                        }
                    });
                    dataInfo+=currentId+","+moneyFun.toNumber(money)+";";
                }
            });
            sureData();
        }else{
            $('#infoTable tbody tr').each(function(i){
                var currentId = $(this).attr('id');
                if(currentId!=undefined){
                    pIds+=currentId+",";
                    var money="";
                    $(this).children('td').each(function(j){
                        if(j==8){
                            if($("#pledgeAttr").val()=='0'){
                                money=$(this).text();
                                totalMoney+= moneyFun.toNumber(money);
                            }
                            if($("#pledgeAttr").val()=='1'){
                                money=$(this).find(".money").val();
                                totalMoney+=moneyFun.toNumber(money);
                            }
                        }
                    });
                    dataInfo+=currentId+","+moneyFun.toNumber(money)+";";
                }
            });
            submitData();
        }
    });
});
bt.guaPledgeInfoBeforeClick=function(){
    var corpId=$("#corpId").val();
    var curId=$("#curId").val();
    var startDate = $("#startDate").val();//担保开始日期
    var endDate = $("#endDate").val();//担保结束日期
    var pledgeAttr=$("#pledgeAttr").val();
    var pDids=$("#pDids").val();
    parameters.corpId=corpId;
    parameters.curId=curId;
    parameters.startDate=startDate;
    parameters.endDate=endDate;
    parameters.pledgeAttr=pledgeAttr;
    parameters.pDids=pDids;
    bt.guaPledgeInfo.set(parameters);
    return true;
}
//抵质押物信息
function guaPledgeInfoCallback(data,dom){
    var ids;
    $.each(data,function (key, val) {
            if(key==0){
                ids=val.id;
            }else if(key>0){
                ids=ids+","+val.id;
            }
        }
    )
    //保存id
    $("#pledgeIds").val(ids);
    refreshTable(data);//刷新表格
}
//json转url参数
var parseParam = function(param, key) {
    var paramStr = "";
    if (param instanceof String || param instanceof Number || param instanceof Boolean) {
        paramStr += "&" + key + "=" + encodeURIComponent(param);
    } else {
        $.each(param, function(i) {
            var k = key == null ? i : key + (param instanceof Array ? "[" + i + "]" : "." + i);
            paramStr += '&' + parseParam(this, k);
        });
    }
    return paramStr.substr(1);
};

/**
 * 使用ajax实现表格的局部刷新
 */
function refreshTable(data){
    var str1 = "";
    $.each(data, function(index, item){
        if(!bt.empty(item)&&!$("#"+item.id).length){//判断是否已经存在了相应的的行
            str1 +='<tr id="'+item.id+'">'+
                '<td style="text-align:center">'+bt.dict("guarantee.pledge.TypeAttr",item.pledgeAttr)+'</td>'+
                '<td style="text-align:center">'+item.pledgeNo+'</td>'+
                '<td style="text-align:center">'+item.typeName+'</td>'+
                '<td style="text-align:center">'+item.pledgeName+'</td>'+
                '<td style="text-align:center">'+item.startDate+'</td>'+
                '<td style="text-align:center">'+item.endDate+'</td>'+
                '<td style="text-align:right">'+bt.money.format(item.pledgeMoney)+'</td>'+
                '<td style="text-align:right">'+bt.money.format(item.availMoney)+'</td>';
            //以下是此次占用的金额
            if(item.pledgeAttr=='1'){
                str1+='<td align="center">';
                str1+='<input type="text" data-name="money'+item.id+'" class="layui-input bt-biz-moneyType money ignore-required-icon" data-decimal="2" data-minus="false" lay-verify="required|number2">';
                str1+='<input type="hidden" name="availMoneys" class="availMoney" value="'+item.availMoney+'">';
                str1+='</td>';
            }else{
                str1+='<td style="text-align:right">'+bt.money.format(item.availMoney);//此次占用金额
                str1+='<input type="hidden" name="money'+item.id+'" class="bt-biz-moneyType money" value="'+item.availMoney+'">';
                str1+='</td>';
            }//以上是此次占用的金额
            str1+='<td style="text-align:center"><button type="button" class="layui-btn del" style="height: auto;">'+bt.lang("common.button.delete")+'</button></td>'+
                +'</tr>'
        }
    });
    $("#pledgeInfo").append(str1);
    bt.moneyType.init();//金额
}

/**
 * 删除相应的行
 */
$("#infoTable").on('click','tr .del',function(){
    $(this).parents('tr').remove();
    var pDids=$("#pDids").val();
    if(!bt.empty(pDids)){//选择抵押物后点击确定，删除后重新选择
        var pDid=$(this).parents('tr').attr("id");//删掉的抵押物id
        pDids=pDids.replace(pDid+",","");
        $("#pDids").val(pDids);
    }
});

/**
 * 将汇总数据回写给前一个页面
 */
var submitData = function(){
    var parentWindow = bt.navtab.getParentDom();
    var type = $("#pledgeAttr").val();
    if(parentWindow){
        parentWindow.updateInfo(dataInfo,type,totalMoney,pIds)
    }
    if(bt.navtab){
        bt.navtab.closeCurrent();
    }
}

var sureData= function(){
    var parentWindow = bt.navtab.getParentDom();
    if(parentWindow){
        parentWindow.updatePledgeInfo(totalMoney,dataInfo,pIds,pledgeMoney)
    }
    if(bt.navtab){
        bt.navtab.closeCurrent();
    }
}

/**
 * 判断输入的金额是否超过了可用价值
 */
$("#infoTable").on('blur','tr .money',function(){
    var avail=$(this).parents("tr").find(".availMoney").val();//可用价值
    var inputmoney=$(this).parents("tr").find(".money").val();//用户输入的金额
    var availMoney = moneyFun.toNumber(avail);
    var money = moneyFun.toNumber(inputmoney);
    if(money>availMoney){
        layer.alert(bt.lang("gua.contractInfo.checkMoney"),{icon:2});
        $(this).parents("tr").find(".money").val(0);
    }
});


function getTableDataIds() {
    var ids = [];
    $('#infoTable tbody tr').each(function(i){
        if($(this).attr('id')){
            ids.push($(this).attr('id'))
        }
    })
    return ids
}
