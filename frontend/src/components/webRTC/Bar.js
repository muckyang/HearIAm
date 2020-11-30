import { Bar, mixins } from 'vue-chartjs'

export default {
    extends: Bar,
    mixins: [mixins.reactiveProp],
    props: ['chartData', 'options'],
    mounted() {
        // console.dir(this.options);
        this.renderChart(this.chartData, this.options)
    },
}