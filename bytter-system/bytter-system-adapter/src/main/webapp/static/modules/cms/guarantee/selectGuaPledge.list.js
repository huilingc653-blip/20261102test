var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    // 获取父页面的ids
    var parentWin = bt.navtab.getCurrentDom();
    var parentTableDataIds = parentWin.getTableDataIds();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[7,'asc']],
        disableIds:parentTableDataIds,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/guarantee/pledgeInfo/passData"
        },
        buttons: [
            {
                text: bt.lang('common.button.sure'),
                action: function ( e, dt, node, config ) {
                    transferData();
                }
            },
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
                  {data:"corpName",bSortable:true},
                  {data:"pledgeAttr",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guarantee.pledge.TypeAttr",full.pledgeAttr);
                      }
                  },
                  {data:"pledgeNo",bSortable:true},
                  {data:"typeName",bSortable:true},
                  {data:"pledgeName",bSortable:true},
                  {data:"startDate",bSortable:true},
                  {data:"endDate",bSortable:true},
                  {data:"curName",bSortable:true},
                  {data:"pledgeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.pledgeMoney);
                      }
                  },
                  {data:"freezeMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.freezeMoney);
                      }
                  },
                  {data:"occupyMoney",bSortable:true,className:'textRight',
                	  render: function ( data, type, full, meta ) {
                    		return bt.money.format(full.occupyMoney);
                        }
                  },
                  {data:"availMoney",bSortable:true,className:'textRight',
                      render: function ( data, type, full, meta ) {
                  		return bt.money.format(full.availMoney);
                      }
                  }
        ]
    });

  //点击提交数据到前台页面
    transferData=function(){
    	 var info=[];
    	 //内置方法返回选中的数据
         var data=bt.datatable.getSelectData('#appTable');
    	  if(data.length>0){
    		  for(var i=0;i<data.length;i++){
    		    var guaPledgeInfoData={
    		      id:data[i].id,
    		      pledgeAttr:data[i].pledgeAttr,
    		      pledgeNo:data[i].pledgeNo,
    			  typeName:data[i].typeName,
    			  pledgeName:data[i].pledgeName,
    			  startDate:data[i].startDate,
    			  endDate:data[i].endDate,
    			  pledgeMoney:data[i].pledgeMoney,
    			  availMoney:data[i].availMoney
    			}
    		    info.push(guaPledgeInfoData);
    		  }
    	  }else{
    		  info.push("");
    	  }
    	 var currentParentIframe = top.$('.layui-tab-content > .layui-show').find('iframe')[0]['name'];
         var func = top.frames[currentParentIframe].window.btSelectCallBack['bt-biz-guaPledgeInfo']['callback'];
           if (func) {
               var data =info;
               func(data);
           }
    }
});


