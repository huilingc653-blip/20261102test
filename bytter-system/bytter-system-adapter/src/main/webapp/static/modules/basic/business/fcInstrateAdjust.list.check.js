var datatable;
bt.use(['form','datatable','selectType','rateType','corp','money','bank','date','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.selectType.init();
    bt.bank.init({
        elem:'.bank',
    });
    bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:false,order: [[7,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/basic/business/fcinstrateadjust/checkDataTable"
        },
        buttons: [
        ]
        ,columns: [
            {data:"netId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    var poolSign = $("#poolSign").val();
                    if(poolSign === "1"){
                        return full.netPoolName;
                    }else{
                        return full.netName;
                    }
                }
            },
            {data:"subjectId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    var poolSign = $("#poolSign").val();
                    if(poolSign === "1"){
                        return full.subjectPoolName;
                    }else{
                        return full.subjectName;
                    }
                }
            },
            {data:"account",bSortable:true},
            {data:"curId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.curName;
                }
            },
            {data:"interestId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.interestName;
                }
            },
            {data:"oldInstrate",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.rateType.getFun().format(full.oldInstrate,'percent');
                }
            },
            {data:"newInstrate",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.rateType.getFun().format(full.newInstrate,'percent');
                }
            },
            {data:"adjustTime",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.date.format(full.adjustTime, "yyyy-mm-dd")
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="check(this)">'+bt.lang("common.button.check")+'</a>';
                return html;
            }}
        ]
    });

    /**
     * 资金池回调
     * @param data
     */
    netIdCallback = function (data) {
        // 重新设置资金池的子单位
        var corpPoolChildUrl = basePath + "/bankacc/btbankaccrelation?getUserCorpPoolChilds&poolId=" + data.id;
        bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
    }


});

function netChange(id){

}

function check(el){
    var poolSign = $("#poolSign").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.check.data'),basePath + "/basic/business/fcinstrateadjust/check/"+poolSign +"?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/basic/business/fcinstrateadjust/update/"+ poolSign + "?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function beforeInit(text,data) {
    if(data.subjectCode){
        return "("+data.subjectCode+")"+data.subjectName;
    }
    return text;
}

/**
 * 资金池回调
 * @param data
 */
function netIdCallback(data) {
    netIdCallback(data);
}