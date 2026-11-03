bt.use(['element'], function(){
    var element = layui.element();
    bt.jquery("#billIframe").attr("src", basePath+"/bill/accebill/abbillInstructionManage/listTestBill");
    bt.jquery("#poolBailIframe").attr("src", basePath+"/bill/pool/abbillInstructionManage/listTestPoolBail");
    bt.jquery("#poolLimitIframe").attr("src", basePath+"/bill/pool/abbillInstructionManage/listTestPoolLimit");
    bt.jquery("#poolConIframe").attr("src", basePath+"/bill/pool/abbillInstructionManage/listTestPoolCon");
    var billIframe;
    var poolBailIframe;
    var poolLimitIframe;
    var poolConIframe;
    //监听Tab切换
    element.on('tab(billExc)', function(data) {
        //data.index=0 兑付申请  data.index=1 兑付跟踪
        if(data.index==0&&!billIframe){
        	billIframe=true;
            refresh(data.index);
        }else if(data.index==1&&!poolBailIframe){
        	poolBailIframe=true;
            refresh(data.index);
        }else if(data.index==2&&!poolLimitIframe){
        	poolLimitIframe=true;
            refresh(data.index);
        }else if(data.index==3&&!poolConIframe){
        	poolConIframe=true;
            refresh(data.index);
        }
    });

    //刷新列表数据
    function refresh(tableIndex){
        var select = parent?$(parent.document): $(document);
        var items = select.find('.layui-tab-content .layui-tab-item');
        bt.each(items,function () {
            var iframe = $(this).find('iframe')[0];
            if(iframe){
                var fname = iframe.name;
                if( fname == '兑付确认' ){
                    var iframeDom = top.frames[fname];
                    var iframeId = '#applyiframe';
                    if(tableIndex==1){
                        iframeId = '#trackiframe';
                    }
                    var innerIframe = iframeDom.$(iframeId)[0];
                    if(innerIframe){
                        var name = innerIframe.name;
                        var applyiframe = iframeDom.frames[name];
                        applyiframe.$.fn.dataTable.tables( { visible: true, api: true, ie:true } ).ajax.reload(null, false);//null false代表页码不重置
                        return;
                    }
                }
            }
        })
    }

});