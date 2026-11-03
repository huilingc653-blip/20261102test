// layui.use(['carousel', 'form','datatablecore','money','capitalMap'], function(){
layui.use([ 'form','datatablecore'], function(){
    // var carousel = layui.carousel;
    //常规轮播
    // carousel.render({
    //     elem: '#panorama-map',
    //     arrow:'none',
    //     anim: 'fade',
    //     width: 'auto'
    //     ,height: '720px',
    //     autoplay:false
    // });
    $('#carouselBtn').on('click','li',function () {
        var id = $(this).data('key')
        $('#carouselBtn').find('li').removeClass('layui-this');
        $(this).addClass('layui-this');
        $('#panorama').find('.carousel-iframe').hide();
        $('#'+id).show()
    })

    // 首页顶部选项卡选择回调
    var capiTalMap = null;
    $("#tabsHeader").on("click",".tabs-title-item",function () {
        $(this).siblings(".tabs-title-item").removeClass("active");
        $(this).addClass("active");
        $("#tabsContent").find(".body-tab-item").addClass("hide");
        var id = $(this).data("id");
        // 如果是全景透视,且已经初始化了，即已加载了iframe
        /**
        *  <div class="carousel-iframe iframe-bal" id="balIframe">
         <iframe src="${basePath}/bi/homeBankAccBalDistribute/list" frameborder="0" name="homeBankAccBalDistribute"></iframe>
         </div>
         <div class="carousel-iframe iframe-dtl" id="dtlIframe">
         <iframe src="${basePath}/bi/homeBankAccDtlDistribute/list" frameborder="0" name="homeBankAccDtlDistribute"></iframe>
         </div>
        * */
        if(!$("#"+id).hasClass('init')){
            if(id === 'panorama'){// 全景透视
                $('#balIframe').html('<iframe src="'+basePath+'/bi/homeBankAccBalDistribute/list" frameborder="0" name="homeBankAccBalDistribute"></iframe>')
                $('#dtlIframe').html('<iframe src="'+basePath+'/bi/homeBankAccDtlDistribute/list" frameborder="0" name="homeBankAccDtlDistribute"></iframe>')
            }else if( id === 'earlyWarning'){ // 指标预警
                $('#earlyIframe').html('<iframe src="'+basePath+'/fundwarn/btwarndata/homeWarnData" frameborder="0" name="homeBankAccBalDistribute"></iframe>')
            }
        }

        $("#"+id).removeClass("hide").addClass('init');
        // if (id == 'panorama' && !capiTalMap){
        //     capiTalMap = bt.capitalMap.init({
        //         clickChinaMapCallback:function (param,myChart,moneyUnit) {
        //             var provinceData;
        //             $.ajax({
        //                 type: "post",
        //                 url: basePath + "/bi/synthesize/moneyMap/getProvinceMapData",
        //                 async: false, //同步执行
        //                 data: {
        //                     "provName": param.name,
        //                     "moneyUnit": moneyUnit
        //                 },
        //                 dataType: "json", //返回数据形式为json
        //                 success: function (result) {
        //                     provinceData = JSON.parse(result);
        //                 }
        //             });
        //
        //             return provinceData
        //         }
        //     })
        // }else{
            //设置地图的自适应模功能
            // if ($('#main').is(":visible")){ //判断是不是世界地图或中国地图或大洲地图
            //     if (window.biz && window.biz['myChart'] && window.biz['myChart']['world']){
            //         window.biz['myChart']['world'].resize();
            //         $("#currencies").removeClass("hide");//显示下拉选择币类
            //     }
            // }else if ($('#province1').is(":visible")){　//判断是不是省份柱状地图或折线图
            //     if (window.biz && window.biz['myChart'] && window.biz['myChart']['barChart']){
            //         window.biz['myChart']['barChart'].resize();
            //     }
            //     $("#currencies").addClass("hide");//显示下拉选择币类
            // } else if ($('#province2').is(":visible")){//判断是不是省份饼图
            //     if (window.biz && window.biz['myChart'] && window.biz['myChart']['pieChart']){
            //         window.biz['myChart']['pieChart'].resize();
            //     }
            //     $("#currencies").addClass("hide");//显示下拉选择币类
            // }
        // }
    })
});
