var billTable,selectTree,$,form,dicts,corp;
bt.use(['form','corp','datatable','dicts','selectTree','mulselect','moneyType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    selectTree=bt.selectTree.init({
        url:basePath+"/ttpay/merchant/treeData",
        elem:".merchantNo-selectTree-checkbox",
        valueField:'merchantNo',
        name: 'ins_merchantNo',
        checkType:'check'
    });
    initMerchantNoTree = function () {
    	var corpId=corp.get('corpId').val();
    	var payType=dicts.get('payType').val();
    	//selectTree.setUrl(basePath + "/ttpay/merchant/treeData?corpId="+corpId+"&payType="+payType);
    }
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        isInitData: false,
        order: [[4,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ttpay/payBillBal/datatable"
        },
        buttons: []
        ,columns: [
            {data:"corpName",bSortable:true,
                render:function(data, type, full, meta){
                	var corp=full.corpName;
                	if(full.corpCode){
                		corp="("+full.corpCode+")"+corp
                	}
                    return corp;
                }
            },
            {data:"payTypeName",bSortable:true},
            {data:"merchantNo",bSortable:true,render: function (data, type, full, meta) {
                    return full.merchantNo + "/" + full.mchId;
            }},
            {data:"balDate",bSortable:true},
            {data:"availableAmount",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"freezeAmount",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"totalAmount",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                if(data==null){
                    return null;
                }else{
                    return bt.money.format(data);
                }

                }
            }
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'5',
                    dataName:'availableAmount'
                },
                {
                    colNum:'6',
                    dataName:'freezeAmount'
                },
                {
                    colNum:'7',
                    dataName:'totalAmount'
                }
            ]
        }
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
});

