var datatable;
var selectTypes;
var mulselectType;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType', 'mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框
    mulselectType = bt.mulselect.init();
  //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    
    //生成列表  
    datatable = bt.datatable.init({  
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[7,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/letter/guarantee/guaGuaranteeInfo/LgDatatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('gua:guarantee:info:apply'),//新增权限
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
                  {data:"guaranteeNo",className:'center',bSortable:true},
                  {data:"corpName",bSortable:true,className:'center'},
                  {data:"guaranteeType",className:'center',bSortable:true,
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guarantee.Lg.TypeAttr",full.guaranteeType);
                      }
                  },
                  {data:"curName",className:'center',bSortable:true},
                  {data:"openCorpName",className:'center',bSortable:true},

                  {data:"incomeCorp",className:'center',bSortable:true},
                  {data:"guaranteeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.guaranteeMoney);
                      }
                  },
                  {data:"startDate",className:'center',bSortable:true},
                  {data:"endDate",className:'center',bSortable:true},
                  {data:"status",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guaGuarantee.status",full.status);
                      }
                  },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('gua:guarantee:info:apply',full.corpId) && full.status!=11){//更新和删除权限
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode"></span>';
                 }
                if(bt.isResource('gua:guarantee:info:query',full.corpId)){//查看和日志权限权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>'; 
              	}
                  return html;
            }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '6', //汇总列序号
                    dataName: 'guaranteeMoney' //汇总列保函金额
                }
            ]
        }
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/letter/guarantee/guaGuaranteeInfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/letter/guarantee/guaGuaranteeInfo/add",
        end:function(){
        	datatable && datatable.draw();
        }
    });
}


//查看保函申请详情
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
	    if(bt.navtab){
	        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/letter/guarantee/guaGuaranteeInfo/view/" +data.id);
	        return;
	    }
	}
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/letter/guarantee/guaGuaranteeInfo/update/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/letter/guarantee/guaGuaranteeInfo/update/" + data.id,
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
        $.post(basePath + "/letter/guarantee/guaGuaranteeInfo/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
