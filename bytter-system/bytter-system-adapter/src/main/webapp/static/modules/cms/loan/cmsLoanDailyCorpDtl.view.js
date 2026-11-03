bt.use(['form','datatable','money'], function(){
    $ = bt.jquery,form = bt.form();

    /**
     * 设置echar图
     * @param data
     */
    var legendsData = $("#legendsData").val();
    var seriesData=$("#seriesData").val();
    var option = {
        title: {
            text: bt.lang("table.report.loansDistribute"),//贷款分布
            left: 'center'
        },
        toolbox:{show:true,feature:{saveAsImage:{show:true,type:"png"},
                restore:{show:true}}},
        legend: {
            type: 'scroll',
            orient: 'vertical',
            right: '10%',
            top: '5%',
            data: $.parseJSON(legendsData)
        },
        series: [
            {
                name: bt.lang("cms.report.loanType"),//融资种类
                type: 'pie',
                radius: '70%',
                center: ['50%', '50%'],
                itemStyle:{
                    normal:{
                        label: {
                            formatter: '  {b|{b}:}{c}  {per|{d}%} ',
                            rich: {
                                b: {
                                    fontSize: 14,
                                    lineHeight: 20
                                },
                                per: {
                                    padding: [2, 4],
                                    borderRadius: 2
                                }
                            }
                        }
                    },
                },
                data: $.parseJSON(seriesData)
            }
        ]
    };
    echarts.init(document.getElementById("chartSum")).setOption(option);
});