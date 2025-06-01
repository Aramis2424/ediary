<template>
  <div class="h-screen">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<script setup lang="ts">
import { Bar } from 'vue-chartjs'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale,
} from 'chart.js'

import type { ChartData, ChartOptions } from 'chart.js'

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale)


const overlaySmallerBars = {
  id: 'overlaySmallerBars',
  afterDatasetsDraw(chart: { 
        ctx: any; 
        getDatasetMeta: (arg0: number) => any; 
        data: { datasets: { data: number[] }[] };}) {
    const ctx = chart.ctx;
    const metaA = chart.getDatasetMeta(0);
    const metaB = chart.getDatasetMeta(1);
    const dataA = chart.data.datasets[0].data as number[];
    const dataB = chart.data.datasets[1].data as number[];
    console.log(metaA);
    

    for (let i = 0; i < dataA.length; i++) {
      const barA = metaA.data[i];
      const barB = metaB.data[i];
      const drawFirst = dataA[i] < dataB[i] ? barA : barB;
      if (dataA[i] > 4) {
        //console.log("HELLO");
        drawFirst._dataset.backgroundColor="red"
      }
      drawFirst.draw(ctx);
    }
  },
}
ChartJS.registry.plugins.register(overlaySmallerBars)

const chartData: ChartData<'bar', number[], string> = {
  labels: ['Янв', 'Фев', 'Мар', 'Апр'],
  datasets: [
    {
      label: 'Настроение',
      data: [8, 7, 8, 6],
      backgroundColor: 'blue',
      borderRadius: 25
    },
    {
      label: 'Продуктивность',
      data: [8, 9, 7, 5],
      backgroundColor: 'green',
      borderRadius: 25
    },
  ],
}

const chartOptions: ChartOptions<'bar'> = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      position: 'top',
    },
    tooltip: {
      mode: 'index',
      intersect: false,
    },
  },
  scales: {
    x: {
      stacked: true,
      offset: true,
    },
    y: {
      min: 1,
      max: 10,
      ticks: {
        stepSize: 1
      },
      beginAtZero: true,
    },
  },
}
</script>
