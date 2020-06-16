function loadStatistics(){

    if(sessionStorage['loggedin'] === 'true'){
        connectStatistics()
    }

    google.charts.load('current', {'packages':['bar']});
    // google.charts.setOnLoadCallback(drawChart);
   
}

function draw(){
    var data = google.visualization.arrayToDataTable([
        ['Year', 'Sales', 'Expenses', 'Profit'],
        ['2014', 1000, 400, 200],
        ['2015', 1170, 460, 250],
        ['2016', 660, 1120, 300],
        ['2017', 1030, 540, 350]
      ]);


    var options = {
        chart: {
        title: 'Website Statistics',
        },
        bars: 'horizontal' // Required for Material Bar Charts.
    };

    var chart = new google.charts.Bar(document.getElementById('barchart_material'));

    chart.draw(data, google.charts.Bar.convertOptions(options));
    document.getElementById('statisticsForm').style.display = 'none'

}


function prepareData(){

}

function drawChart(message) {

    data = JSON.parse(message.body)
    console.log(data)
    var preprocessed_data = prepareData(data);

    var options = {
        chart: {
        title: 'Website Statistics',
        },
        bars: 'horizontal' // Required for Material Bar Charts.
    };

    var chart = new google.charts.Bar(document.getElementById('barchart_material'));

    chart.draw(preprocessed_data, google.charts.Bar.convertOptions(options));

    document.getElementById('statisticsForm').style.display = 'none'

}


function dateSelection(){
    document.getElementById('statisticsForm').style.display = 'block'
}