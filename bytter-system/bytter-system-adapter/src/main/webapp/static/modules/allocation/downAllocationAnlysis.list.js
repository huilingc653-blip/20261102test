bt.use(['form', 'bttable', 'dateType', 'selectType', 'moneyType', 'dicts', 'bank', 'corp','page','freezeTable'], function () {
    $ = bt.jquery, form = bt.form();
    corpInit = bt.corp.init();
    dateType=bt.dateType.init();
    selectType=bt.selectType.init();
    dicts=bt.dicts.init();
    moneyType=bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    var height = $(window).height();
    var tableHeight = height - 280;
    if (tableHeight < 380){
        tableHeight = 380;
    }

    //生成列表
    table = bt.bttable.init({
        elem: "#appTable",
        scrollY:tableHeight+"px",
        url: basePath+"/report/downAllocationAnalysis/loadData",
        urldata:getParam(),
        columns: [
            {data: "billNo", bSortable: true},
            {data: "voucherName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true,
                render: function ( data, type, full, meta ) {
                     if(isNull(data.payerAcc)){
                         return "";
                    }else{
                         return data.payerAcc;
                     };
                }
            },
            {data: "payerAccName", bSortable: true,
                render: function ( data, type, full, meta ) {
                    if(isNull(data.payerAcc)){
                        return "";
                    }else{
                        return data.payerAccName;
                    };
                }
            },
            {data: "payerBank", bSortable: true,
                render: function ( data, type, full, meta ) {
                    if(isNull(data.payerAcc)){
                        return "";
                    }else{
                        return data.payerBank;
                    };
                }
            },
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "payMoney", className:"textRight",bSortable: true,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(data.payMoney);
                }
            },
            {data:"cbsBillNo",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                	var html = "";
                	if(data.cbsBillNo!="" && data.cbsBillNo!=null){
                		html = "<a href='javaScript:void(0)' onclick='viewBill(\""+data.cbsBillId+"\",\""+data.voucherFrom+"\")'>"+data.cbsBillNo+'</a>';
                	}
                	return html;
                }
            },
            {data: "purpose", bSortable: true},
            {data: "status",bSortable:true,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.status",data.status);
                }
            },
            {data: null, bSortable: false, className: 'center',
            render: function (data, type, full, meta) {
                return "<a href='javaScript:void(0)' onclick='view(\""+data.billNo+"\",\""+data.payerBank+"\")'>" + bt.lang("common.button.view") + "</a> ";
            }
            }
        ],
        tableInitCallback:function(data){
            pageTemp(data);
            headOtherInfo(data);
            tatalBal(data);
        },
        tableDrawCallback:function (data) {
            pageTemp(data);
            sumBal(data);
            tatalBal(data);
        }
    });

    // bt.freezeTable.init({
    //     elem:'#appTable',//表格id
    //     freezeRowNum:'1',//冻结几行
    //     freezeColumnNum:'1',//冻结几列
    //     height:tableHeight+'px' //表格高度
    // })

    //监听提交
    form.on('submit(search)', function(data) {

        if(table){
            $("#currentPage").val(1);
            table.draw({
                url: basePath+"/report/downAllocationAnalysis/loadData",
                urldata:getParam()
            })
        }

    });
    headOtherInfo=function(data){
        var balance=0;
        layui.each(data,function(i,d){
            balance+=parseFloat(d.payMoney);
        })
        if(data){
            if(data.length){
                var bankHtml = "";
                bankHtml += "<tr>";
                bankHtml += "<td align='right' width='20%' rowspan = "+data[0].rowSize+">"+bt.lang('downAllocationAnalysis.totalBisAccBal')+"</td>";
                layui.each(data[0].bankList,function(i,d){
                    var typeName = d.typeName;
                    var availBal = d.availBal;
                    if (!typeName || typeName == "") {
                        typeName = d.TYPENAME;
                        availBal = d.AVAILBAL;
                    }
                    bankHtml += "<td id='bankName' align='right' width='20%'><span id='bn'>"+typeName+"</span></td>";
                    bankHtml += "<td id='balance' align='left' width='20%'><span id='ba'>"+moneyFun.format(availBal)+"</span></td>";
                    if(i % 2 !== 0){
                        bankHtml += '</tr>';
                        if(i < data[0].bankList.length ){
                            bankHtml += '<tr>';
                        }
                    }
                })
                if(data[0].bankList.length % 2 !== 0){
                    bankHtml +="<td></td><td></td>";
                }
                $("#search").append(bankHtml);
            }else{
                //清空合计
                $("#sum").each(function(){
                    $(this).children('td').eq(3).text('');
                });
            }
             $("#sum").find('td[name="sumBalance"]').text(moneyFun.format(balance));
        }
    }

    form.on('submit(excel)', function(data) {
        var url = basePath + "/report/downAllocationAnalysis/doExportInterestBill";
        DownLoadFile({url:url,data:data.field,method:'post'});
        return false;
    });

    var DownLoadFile = function (options) {
        var config = $.extend(true, { method: 'post' }, options);
        var $iframe = $('<iframe id="down-file-iframe" />');
        var $form = $('<form target="down-file-iframe" method="' + config.method + '" />');
        $form.attr("action", config.url);
        for (var key in config.data) {
            $form.append('<input type="hidden" name="' + key + '" value="' + config.data[key] + '" />');
        }
        $iframe.append($form);
        $(document.body).append($iframe);
        $form[0].submit();
        $iframe.remove();
    }
});

/**
 * 明细合计
 * @param data
 */
var sumBal = function(data){
    var bal = 0;
    layui.each(data,function(i,d){
        bal+=parseFloat(d.payMoney);
    })
    $("#sum").find('td[name="sumBalance"]').text(moneyFun.format(bal));
}

/**
 * 下拨单位和下拨金额合计
 * @param data
 */
var tatalBal = function (data) {
    if(isNull(data)){
        $("#downCorp").html("");
        $("#total").find('td[name="totalBal"]').text(moneyFun.format(0));
    }else{
        $("#downCorp").html(data[0].corpName);
        $("#total").find('td[name="totalBal"]').text(moneyFun.format(data[0].total));
    }
}
/**
 * 获取参数
 * @returns {string}
 */
var getParam = function(){
    var param="";
    param += "corpId="+corpInit.get('corpId').val()+
        "&gedApplyDate="+dateType.get('gedApplyDate').val()+"&ledApplyDate="+dateType.get('ledApplyDate').val()+
        "&voucherType="+dicts.get('voucherType').val()+
        "&gegPayMoney="+moneyType.get("gegPayMoney").val()+"&legPayMoney="+moneyType.get("legPayMoney").val()+
        "&start="+$("#currentPage").val();
    return param;
}
var page = null;
var table = null;
var pageTemp = function (data) {
    if(!page && data){
        var pages;
        if(data.length){
            pages=data[0].count;
        }else{
            pages=1;
        }
        page = bt.page.init({
            elem:'#page',//分页组件容器
            pages:pages,
            count:data.length, //表格总页码
            currentPage:$("#currentPage").val(),//当前页码，默认值为 0,即第一页
            len : data.length, //当前显示条数
            lengthMenu:false,
            custom:{
                start:1
            },
            jump:function(obj){ //翻页回调方法
                var curr = obj.curr;
                var len = obj.len;
                $("#currentPage").val(curr);
                if(table){
                    table.draw({
                        url: basePath+"/report/downAllocationAnalysis/loadData",
                        urldata:getParam()

                    })
                }
            }
        })
    }else{
        var elem = $("#page").find('.wrapper');
        var pages;
        var currentPage = $("#currentPage").val();
        var start = 1;
        if(data.length){
            pages = data[0].count;
        }else{
            pages = 1;
            currentPage = 1;
            start = 0;
        }
        elem.find('.total').text(pages);
        elem.find('.table-record .table-startRecord1').text( start );
        elem.find('.table-pageNumber').val(currentPage);//更新当前页码
        page.updateStyle(elem.find('.table-record '),currentPage,pages);//更新样式
        elem.find('.table-record .table-endRecord1').text( data.length );//更新结束数据条数
        elem.find('.table-record .table-totalRecord1').text( data.length );//更新总条数
    }
}

/**
 * 查看
 * @param billNo
 */
function view(billNo,payerBank){
    parent.layer.open({
        type: 2,
        area: ['1120px', '700px'],
        maxmin: true,
        content: basePath + "/report/downAllocationAnalysis/view?billNo=" + billNo+"&payerBank="+payerBank
    });
}

/**
 * 判断为空
 * @param data
 * @returns {*}
 */
var isNull = function (data) {
    if(data == null || data == "null" || data == "undefined" || data.length < 1){
        return true;
    }else{
        return false;
    }
}

//查看业务单据详情
function viewBill(cbsBillId,voucherFrom){
    if(cbsBillId.indexOf("_D") > -1){
        cbsBillId = cbsBillId.replace("_D", "");
    }
    var action = "/netbank/nisbillhead/view/" + cbsBillId;
    //资金调拨
    if(voucherFrom != "1" && voucherFrom != "10"){
        action = "/allocation/nisallocationreq/view/" + cbsBillId;
    }
    //代发工资
    if(voucherFrom == "10"){
        action = "/wage/niswage/view/" + cbsBillId;
    }

    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + action
    });
}
