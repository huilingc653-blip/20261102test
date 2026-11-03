var billTable,selectTree,$,form,dicts,corp;
bt.use(['form','corp','datatable','dicts','selectTree','mulselect','moneyType','dateType'], function(){
    $ = bt.jquery,form = bt.form();
    selectTree=bt.selectTree.init({
        url:basePath+"/ttpay/merchant/treeData",
        elem:".merchantNo-selectTree-checkbox",
        valueField:'merchantNo',
        name: 'ins_mchId',
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
        order: [[7,'desc'],[3,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/ttpay/payBillDtl/datatable"
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
            {data:"mchId",bSortable:true,render: function (data, type, full, meta) {
                    return full.mchId + "/" + full.appId;
            }},
            {data:"serialNo",bSortable:true},
            {data:"trandNo",bSortable:true},
            {data:"payAcc",bSortable:true},
            {data:"trandTime",bSortable:true},
            {data:"trandMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(data);
		         }
		      },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html='';
            	html+='<a class="" href="#" data-row="'+meta.row+'" onclick="dtlView(\''+full.id+'\',\''+full.payType+'\')">'+bt.lang("common.button.viewDtl")+'</a>';
            	return html;
            }}
        ],
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '8',
                    dataName: 'trandMoney'
                }
            ]
        }
    });
    dicts=bt.dicts.init();
    corp=bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();
});

//跳转至添加页面 
function dtlView(id,payType){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/ttpay/payBillDtl/view?id="+id+"&payType="+payType);
        return;
    }
	parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/ttpay/payBillDtl/view?id="+id+"&payType="+payType,
        end:function(){
        	bt.datatable.refreshCurrentPage();
        }
    });
}


