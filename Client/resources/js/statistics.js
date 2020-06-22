var chart;
var charData;

function loadStatistics(){

    if(sessionStorage['loggedin'] === 'true'){
        connectStatistics()
    }

    google.charts.load('current', {'packages':['bar']});
    // google.charts.setOnLoadCallback(drawChart);
   
}

function updateChart(dailyStats){

    console.log('updaing chart')
    console.log('old data')
    console.log(charData)
    newStats = []
    for(stat in dailyStats){
        newStats.push(dailyStats[stat])
    }

    charData[1] = newStats
    console.log('new data')
    console.log(charData)

    data = google.visualization.arrayToDataTable(charData)
    var options = {
        chart: {
        title: 'Website Statistics',
        },
        bars: 'vertical' // Required for Material Bar Charts.
    };

    chart = new google.charts.Bar(document.getElementById('barchart_material'));
    chart.draw(data, google.charts.Bar.convertOptions(options));
}


function prepareData(data){

    charData = [['Data', 'Guests', 'regularSubs', 'Managers', 'Owners' ,'Admins']]
    
    
    for (day in data){
        values = []
        dayStat = data[day]
        for(value in dayStat){
            values.push(dayStat[value])
        }
        charData.push(values)
    }
    

    console.log(charData)

    return google.visualization.arrayToDataTable(charData)


}

function drawChart(data) {

   
    console.log(data)
    var preprocessed_data = prepareData(data);

    sessionStorage['chartData'] = preprocessed_data

    var options = {
        chart: {
        title: 'Website Statistics',
        },
        bars: 'vertical' // Required for Material Bar Charts.
    };

    chart = new google.charts.Bar(document.getElementById('barchart_material'));

    chart.draw(preprocessed_data, google.charts.Bar.convertOptions(options));

    document.getElementById('statisticsForm').style.display = 'none'

}


function dateSelection(){
    document.getElementById('statisticsForm').style.display = 'block'
}


async function getStatistics(){

    statsURL = "https://localhost:8443/getStats?"

    var from = document.getElementById("fromText").value

    var to = document.getElementById("toText").value

    sessionStorage['toDate'] = to

    statsURL += 'sessionId=' + sessionStorage['sessionId']
    statsURL += '&from=' + from
    statsURL += '&to=' + to 

    console.log(statsURL)

    var result;
    await fetch(statsURL, headers).then(response => response.json()).then(response => result = response)
    console.log(result)
    if(result['resultCode'] === 'SUCCESS'){
        drawChart(result['stats'])
    }

}