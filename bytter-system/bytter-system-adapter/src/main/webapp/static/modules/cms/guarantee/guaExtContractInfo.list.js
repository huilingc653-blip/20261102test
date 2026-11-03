var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','btImport','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    
    // 生成列表
    datatable = bt.datatable.init({  
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[10,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/guaExtContractInfo/datatable"
        },
        buttons: [
            {
                // hide:bt.isnotResource('gua:contract:info:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
                  {data:"corpName",className:'center',bSortable:true},//担保单位
                  {data:"guaNo",className:'center',bSortable:true},//担保编号
                  {data:"guaTypeName",className:'center',bSortable:true},//担保方式
                  {data:"debtorPersonName",className:'center',bSortable:true},//债务人
                  {data:"creditorPerson",className:'center',bSortable:true},//债权人
                  {data:"curName",className:'center',bSortable:true},//币别
                  {data:"guaMoney",bSortable:true,className:'textRight',//担保额度
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.guaMoney);
                      }
                  },
                  {data:"contractCode",className:'center',bSortable:true},//合同编号
                  {data:"startDate",className:'center',bSortable:true},//开始日期
                  {data:"endDate",className:'center',bSortable:true},//结束日期
                  {data:"status",bSortable:true,className:'center',//状态
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guaContract.status",full.status);
                      }
             },
            {data:"drawMoneyDate",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
               if(full.status!=11){//更新和删除权限
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode"></span>';
                }
                // if(bt.isResource('gua:contract:info:query',full.corpId)){//查看和日志权限权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>'; 
               	// }
                  return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8', //汇总列序号
                    dataName: 'guaMoney' //汇总列担保额度
                }
            ]
        }
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/guarantee/guaExtContractInfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaExtContractInfo/add",
        end:function(){
        	datatable && datatable.draw();
        }
    });
}

//查看担保合同信息
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaExtContractInfo/viewInfo/" +data.id
    });
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/guarantee/guaExtContractInfo/update/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/pledgeInfo/update/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//日志列表
function listMsg(el){
  var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
  if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/guarantee/gualog/listlog?guaId=" + data.id);
            return;
        }
  }
}

//删除行
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/guarantee/guaExtContractInfo/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
