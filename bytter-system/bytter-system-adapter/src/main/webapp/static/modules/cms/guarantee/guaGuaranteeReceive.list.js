var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框

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
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/letter/guarantee/guaGuaranteeRecive/datatable"
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
                  {data:"guaranteeNo",className:'center',bSortable:true},
                  {data:"corpName",className:'center',bSortable:true,},
                  {data:"guaranteeType",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guarantee.Lg.TypeAttr",full.guaranteeType);
                      }
                  },
                  {data:"openCorpName",className:'center',bSortable:true},
                  {data:"incomeCorp",className:'center',bSortable:true},
                  {data:"guaranteeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.guaranteeMoney);
                      }
                  },
                  {data:"startDate",className:'center',bSortable:true},
                  {data:"endDate",className:'center',bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('gua:guarantee:info:receive',full.corpId)){//领取
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="receive(this)">'+bt.lang("table.guaGuarantee.recive")+'</a>';
                }
                    if(bt.isResource('gua:guarantee:info:receivefail',full.corpId)){//打回
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="repulse(this)">' + bt.lang("common.button.repulse") + '</a>';
                    }
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
                    colNum: '6', //汇总列序号
                    dataName: 'guaranteeMoney' //汇总列保函金额
                }
            ]
        }
    });
});

//领取
function receive(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.guaGuarantee.recive'), basePath + "/letter/guarantee/guaGuaranteeRecive/receiveGuaGurantee/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/letter/guarantee/guaGuaranteeRecive/receiveGuaGurantee/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//打回
function repulse(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.repulse'), function () {
        $.post(basePath + "/letter/guarantee/guaGuaranteeRecive/repulse/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
