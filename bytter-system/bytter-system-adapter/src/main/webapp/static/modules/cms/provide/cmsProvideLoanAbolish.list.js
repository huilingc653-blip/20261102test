bt.use(['form','datatable','corp','moneyType','dateType'], function(){
    $ = bt.jquery,form = bt.form();

    //单位
    bt.corp.init();

    //时间
    bt.dateType.init();

    //借款金额
    bt.moneyType.init();


    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        colReorder: true,
        ordering:false,
        ajax: {
            'url': basePath+"/cms/provide/abolish/tableCancellation"
        },
        buttons:[],
        columns: [
            {data:"corpName",bSortable:true,className:"center"},
            {data:"bankName",bSortable:true,className:"center"},
            {data:"billCode",bSortable:true,className:"center"},
            {data:"contractNo",bSortable:true,className:"center"},
           // {data:"plAccount",bSortable:true},
            {data:"infoCreateDate",bSortable:true,className:"center"},
            {data:"curName",bSortable:true,className:"center"},
            {data:"plMoney",bSortable:true,className:"textRight",render: function(data,type,full,meta){
          		var plMoney = bt.money.format(full.plMoney);
          		return plMoney;
          	}},
           /* {data:"money",bSortable:true,className:"textRight",render : function(data,type,full,meta){
          		var money = bt.money.format(full.money);
          		return money;
          	}},*/
            {data:"plDate",bSortable:true,className:"center"},
            {data:"repayDate",bSortable:true,className:"center"},
            {data:"repayTypeName",bSortable:true,className:"center",render : function(data,type,full,meta){
                return bt.empty(full.repayTypeName)? full.repayTypeName1:full.repayTypeName;;
            }},
            {data:"plClass",bSortable:true,className:"center",render : function(data,type,full,meta){
            	return full.plClassName;
            }},
            /*{data:"plType",bSortable:true,render : function(data,type,full,meta){
            	return full.plTypeName;
            }},*/
            {data:"plTerm",bSortable:true,className:"center",render :function(data,type,full,meta){
         		var term = full.plTerm +  full.ternUnitName;
                return term;
         	}},
            {data:"status",bSortable:true,className:"center",render : function(data,type,full,meta){
                    return bt.dict("cms.status",full.status);
            }},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html='';
                html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(bt.isResource('cms:provide:cancel:edit')){
                	html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("Finacial.accbook.invalid.button.add")+'</a>';//作废申请
                }
                html +=' \<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                // if(full.id != null && full.id != ""){
             	// 	 html += '\<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
             	//   }
                return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7', //汇总列序号
                    dataName: 'plMoney' //汇总列借款金额
                }
            ]
        }
    });
});

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/provide/abolish/view?infoBillCode=" + data.billCode + "&struts=" + 0
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/provide/abolish/update?infoBillCode=" + data.billCode + "&abolishId="+data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/provide/abolish/update?infoBillCode=" + data.billCode + "&abolishId="+data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/provide/abolish/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}
