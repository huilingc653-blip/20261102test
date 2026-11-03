bt.use(['form','datatable','moneyType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/loan/cmsloanrepay/datatable"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
                  {
                      extend: "colvis",
                      text: bt.lang('common.button.theadHide'),
                      columns: ':not(:first):not(:last)'
                  }
              ],
      fixedColumns: {
                  rightColumns:1//代表冻结右边一列
              },
        columns: [
            {data:"corpName",className:'center',bSortable:false},
            {data:"netName",className:'center',bSortable:true},
            {data:"billCode",className:'center',bSortable:false},
            {data:"createDate",className:'center',bSortable:false},
            {data:"sendCode",className:'center',bSortable:false},
            {data:"repayMoney",className:'textRight',bSortable:false,
        		render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.repayMoney);
                }
            },
            {data:"repayDate",className:'center',bSortable:false},
            {data:"curName",className:'center',bSortable:false,},
            {data:"bizStatus",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.bizStatus",full.bizStatus);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                if(bt.isResource('cms:loan:cmsloanrepay:edit',full.corpId)){
	                html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
	                <span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                }
                html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
               // <span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
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
                    colNum: '6', //汇总列序号
                    dataName: 'repayMoney' //汇总列数据
                }
            ]
        }
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/loan/cmsloanrepay/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/loan/cmsloanrepay/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
    	if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloanrepay/view/" + data.id);
            return;
        }
    	parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloanrepay/view/" + data.id
        });
    }
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/loan/cmsloanrepay/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloanrepay/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/loan/cmsloanrepay/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}