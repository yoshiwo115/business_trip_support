//15818015_InoueYoshiaki
document.addEventListener("DOMContentLoaded", () => {

    //json変換のための受け渡し用にグローバル変数で定義
    //選択した経路
    let selected_route = "";
    //運賃
    let price = 0;

    //dailyAllowanceとAccommodationFee統合して、calcAll
    function calcAll(model) {
            const params = {
                method: "POST",
                headers: {
                    "Content-Type": "application/json; charset=utf-8"
                },
                body: JSON.stringify(model)
                //json形式に変換
            };
            fetch("/calc_All_final", params)
                .then((response) => {
                    return response.json();
                })
                .then((model) => {
                    console.log(model);
                    document.getElementById("daily_allowance").innerText = model.dailyAllowance+"円";
                    document.getElementById("accommodation_fee").innerText = model.accommodationFee+"円";
                })
                .catch((error) => {
                    console.log(error);
                });
    }

    //計算ボタン押したとき
    document.getElementById("calc_all_button").addEventListener("click", () => {
        //.valueで文字列取得
        const name = document.getElementById("name").value;
        const jobTitle = document.getElementById("job_title").value;
        const belonging = document.getElementById("belonging").value;
        const department = document.getElementById("department").value;
        const department2 = document.getElementById("department2").value;
        const purpose = document.getElementById("purpose").value;
        const place = document.getElementById("place").value;
        const place2 = document.getElementById("place2").value;
        const changing = selected_route;
        const fare = price;
        const agenda = "これから決めるよ";

        const departuredatetime = document.getElementById("departuredatetime").value;
        const arrivaldatetime = document.getElementById("arrivaldatetime").value;

        const departuredatetimeObj = new Date(departuredatetime);
        const arrivaldatetimeObj = new Date(arrivaldatetime);

        //msDiff_到着と出発の差
        let msDiff = arrivaldatetimeObj.getTime() - departuredatetimeObj.getTime();
        //daysDiff_時間の差に変換
        let hoursDiff = Math.floor(msDiff / (1000 * 60 * 60));

        let travelHours = parseInt(hoursDiff);
        let travelDays = 0;
        const numberOfNights = document.getElementById("number_of_nights").value;

        //travelHoursが24時間以上なら日としてカウント
        if(travelHours >= 24){
            travelDays = Math.floor(travelHours / 24);
            travelHours = travelHours % 24;
        }

        //jsonで全部送る
        const jsonData = {"belonging":belonging, "department":department
                           ,"department2":department2, "jobTitle":jobTitle
                           ,"name":name, "purpose":purpose, "place":place
                           ,"place2":place2,"travelHours":travelHours
                           ,"travelDays":travelDays, "numberOfNights":numberOfNights
                           ,"changing":changing,"agenda":agenda,"fare":fare};
        calcAll(jsonData);
        console.log(fare);
    });

    //駅すぱあと
    // 駅名入力パーツ初期化
    const departureStationApp = new expGuiStation(document.getElementById("departure_station"));
    departureStationApp.dispStation();
    const arrivalStationApp = new expGuiStation(document.getElementById("arrival_station"));
    arrivalStationApp.dispStation();

    // 日付入力パーツ初期化
    const dateTimeApp = new expGuiDateTime(document.getElementById("dateTime"));
    dateTimeApp.dispDateTime();

    // 経路検索パーツ初期化
    const resultApp = new expGuiCourse(document.getElementById("route_search_result"));

    /*
     * 探索実行時のコールバック関数
     */
    function result(isSuccess) {
        if (isSuccess) {
            console.log("運賃：" + resultApp.getFarePrice());
            document.getElementById("fare").innerText = resultApp.getFarePrice()+"円";
            price = resultApp.getFarePrice();
            console.log("路線リスト：" + resultApp.getLineList());
            console.log("地点リスト：" + resultApp.getPointList());
            console.log("出発日時：" + resultApp.getDepartureDate());
            console.log("到着日時：" + resultApp.getArrivalDate());
            console.log(resultApp.getResultString());
        } else {
            alert("探索結果が取得できませんでした");
        }
    }

    //経路検索ボタンが押されたとき
    document.getElementById("route_search_button").addEventListener("click", () => {
        //コンソールに出力
        console.log(departureStationApp.getStation());
        console.log(arrivalStationApp.getStation());

        // 発着地リスト
        let searchWord = "viaList=" + departureStationApp.getStation()
                                     +":"+ arrivalStationApp.getStation();
        //列車運行日
        searchWord += "&date=" + dateTimeApp.getDate();
        //列車の発着時刻
        searchWord += "&time=" + dateTimeApp.getTime();
        // 探索結果数
        searchWord += "&answerCount=" + String(3);
        // 片道料金
        let one_price = resultApp.PRICE_ONEWAY;
        console.log(searchWord);
        // 探索を実行
        resultApp.search(searchWord, one_price, result);
    });

    //経路選択ボタンが押されたとき
    document.getElementById("select_route_button").addEventListener("click", () => {

        let departure = resultApp.getDepartureDate().getHours() +"時"+
                            resultApp.getDepartureDate().getMinutes() +"分発"
        let arrival = resultApp.getArrivalDate().getHours() +"時"+
                            resultApp.getArrivalDate().getMinutes() +"分着"


        console.log("出発時 : " +resultApp.getDepartureDate().getHours());
        console.log("出発分 : " +resultApp.getDepartureDate().getMinutes());
        console.log("地点リスト : " +resultApp.getPointList());
        const pointList = resultApp.getPointList().split(",");
        for(let i=0; i<pointList.length; i++){
            console.log(pointList[i]);
        }
        console.log("路線リスト: " +resultApp.getLineList());
        const lineList = resultApp.getLineList().split(",");
        for(let i=0; i<lineList.length; i++){
            console.log(lineList[i]);
        }
        console.log("到着日時 : " +resultApp.getArrivalDate());


        let selectList = new Array();
        for(let i=0; i<lineList.length; i++){
            selectList[i] = "~"+lineList[i]+"~"+pointList[i+1];
        }

        //選択した経路の表示
        document.getElementById("selected_route").innerText = departure +" "
            + departureStationApp.getStation() + selectList +" "+ arrival;

        //json用
        selected_route = departure +" "+ departureStationApp.getStation()
                                      + selectList +" "+ arrival;
    });

});

  
  