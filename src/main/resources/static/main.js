var app = angular.module("RobotManagement", []);

// Controller Part
app.controller("RobotController", function($scope, $http) {
      $scope.elements  = ["light sensing"," sound sensing"," temperature sensing"," pressure sensing"," mobility degrees-of-freedom"];

     $scope.selectedValues = ["light sensing"," sound sensing"];
     console.log("selectedValues : "+$scope.selectedValues);
    $scope.functions = [];
    $scope.phases = [];
    $scope.robots = [];
    $scope.robotsperphase = [];
    $scope.robotForm = {
        id: -1,
        robot_id: "",
        name: "",
        year: 1600,
        mass: 0,
        color: "",
        functions: [],
        phase: "IN_DESIGN"

    };

    // Now load the data from server
    _refreshRobotData();

    _getRobotFunctions();
    _getRobotPhases();
    _getRobotCountPerPhase();

    // HTTP POST/PUT methods for add/edit robot
    // Call: http://localhost:8080/robot
    $scope.submitRobot = function() {

    if ($scope.robotForm.id == -1) {
        method = "POST";
        url = '/employee';
    } else {
        method = "PUT";
        url = '/employee';
    }
        url = '/robot';
        var newFunctions = $scope.robotForm.functions;
        if(newFunctions != null || newFunctions != undefined) {
            $scope.robotForm.functions = newFunctions.toString();
        }

        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.robotForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.clearRobot = function() {
        _clearFormData();
    }

    // HTTP DELETE- delete robot by Id
    // Call: http://localhost:8080/robot/{robotId}
    $scope.deleteRobot = function(robot) {
        $http({
            method: 'DELETE',
            url: '/robot/' + robot.robot_id
        }).then(_success, _error);
    };

    // In case of edit
    $scope.editRobot = function(robot) {

        $scope.robotForm.id = 1;
        $scope.robotForm.robot_id = robot.robot_id;
        $scope.robotForm.name = robot.name;
        $scope.robotForm.year = robot.year;
        $scope.robotForm.mass = robot.mass;
        $scope.robotForm.color = robot.color;
        $scope.robotForm.functions = robot.functions.split(",");
        $scope.robotForm.phase = robot.phase;
        document.getElementById("robotId").readOnly = true;

        console.log("editRobot functions : "+$scope.robotForm.functions);
    };

    // HTTP GET- get all robots collection
    // Call: http://localhost:8080/robot
    function _refreshRobotData() {
        $http({
            method: 'GET',
            url: '/robot'
        }).then(
            function(res) { // success
            console.log(res)
                $scope.robots = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    // HTTP GET- get robot functions
    // Call: http://localhost:8080/robot/functions
    function _getRobotFunctions() {
        $http({
            method: 'GET',
            url: '/robot/functions'
        }).then(
            function(res) { // success
                console.log("functions success response : "+res);
                $scope.functions = res.data.functions.split(",");
            },
            function(res) { // error
                console.log("functions error response : "+res);
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    // HTTP GET- get robot phases
    // Call: http://localhost:8080/robot/phases
    function _getRobotPhases() {
        $http({
            method: 'GET',
            url: '/robot/phases'
        }).then(
            function(res) { // success
            console.log(res)
                $scope.phases = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    // HTTP GET- get robot functions
    // Call: http://localhost:8080/robot/count-per-phase
    $scope.getRobotCountPerPhase = function() {
    _getRobotCountPerPhase();
    }

    function _getRobotCountPerPhase() {
        $http({
            method: 'GET',
            url: '/robot/count-per-phase'
        }).then(
            function(res) { // success
                console.log("robots per phase success response : "+res.data);
                $scope.robotsperphase = res.data;
            },
            function(res) { // error
                console.log("robots per phase error response : "+res);
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function _success(res) {
        _refreshRobotData();
        _clearFormData();
    }

    function _error(res) {
        var data = res.data;
        var status = res.status;
        var errorCode = res.data.code;
        var errorMessage = res.data.message;
        var header = res.header;
        var config = res.config;
        alert("Error code : " + errorCode + "\nError message : " + errorMessage);
    }

    // Clear the form
    function _clearFormData() {
        $scope.robotForm.id = -1;
        $scope.robotForm.robot_id = "";
        $scope.robotForm.name = "";
        $scope.robotForm.year = 1600;
        $scope.robotForm.mass = 0;
        $scope.robotForm.color = "";
        $scope.robotForm.functions = [];
        $scope.robotForm.phase = "IN_DESIGN";
        document.getElementById("robotId").readOnly = false;
    };
});