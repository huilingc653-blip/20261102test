var billTable,selectTree,$,form,dicts,corp;
bt.use(['form','corp','datatable','dicts','selectTree','mulselect','moneyType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    selectTree=bt.selectTree.init({
        url:basePath+"/ttpay/merchant/treeData",
        elem:".merchantNo-selectTree-checkbox",
        valueField:'merchantNo',
        name: 'ins_mchId',
        checkType:'check',
        placeholder:bt.lang("ttpay.merchantNo")
    });

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[9,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ttpay/refundOrderQuery/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"retSource",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("ttpay.refund.retSource", data);
                }
            },
            {data:"payTypeName",bSortable:true},
            {data:"voucherStat",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("ttpay.refund.voucherStat", data);
                }
            },
            {data:"mchId",bSortable:true},
            {data:"trandNo",bSortable:true},
            {data:"backNo",bSortable:true},
            {data:"corpName",bSortable:true,
                render:function(data, type, full, meta){
                    var corp=full.corpName;
                    if(full.corpCode){
                        corp="("+full.corpCode+")"+corp
                    }
                    return corp;
                }
            },
            {data:"shopName",bSortable:true},
            {data:"updateDate",bSortable:true},
            {data:"returnMsg",bSortable:true},
            {data:"trandMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"backMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"createName",bSortable:true},
            {data:"checkName",bSortable:true},
            {data:"rmk",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html='';
                if(bt.isResource('ttpay.refundquery.relaunch',full.corpId)){
                    if(full.voucherStat === '2'){
                        html+='<a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="reLaunch(this)">'+bt.lang("ttpay.refund.reLaunch")+'</a>';
                    }
                    if(full.voucherStat === '2' && full.status === '95'){
                        html+='<span class="text-explode">|</span>'+'<a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="refundBack(this)">'+bt.lang("common.button.back")+'</a>';
                    }
                }
                    return html;
                }}
        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '11',
                    dataName: 'trandMoney'
                },
                {
                    colNum: '12',
                    dataName: 'backMoney'
                }
            ]
        }
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
});

// 重新发起
function reLaunch(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('ttpay.refund.confirm.reLaunch'),function(){
        $.post(basePath + "/ttpay/refundOrderQuery/reLaunch",{"id":data.id}, function(result){
            bt.loading.hide();
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

//退款打回编辑页面
function refundBack(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        title:bt.lang('common.button.back'),
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/refundOrderQuery/refundBack?id="+data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}




