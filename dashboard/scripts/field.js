"use strict";
// distance from front to back of the robot, with bumpers
var MessageHandler = require('./MessageHandler');

var config = {
    robot_width: 35.25,
    robot_height: 29.25,
    field_width: 888.0,
    field_height: 360.0,
    field_offset: {
        x: 120.0,
        y: 18.0
    }
}
var ui = {
    field_contailer: document.getElementById("field_container"),
    field: document.getElementById("field"),
    robot: document.getElementById("robot"),
    side: document.getElementById("side"),
    position: document.getElementById("position")
}
var pixels_per_inch = 0;
var robot_offset = {
    x: 0,
    y: 0,
    heading: 0
}
var robot_position = {
    x: 0,
    y: 0,
    heading: 0
}


var configurations = {
    left: {
        left_outer: {
            x: 17.625,
            y: 0,
            heading: 0
        },
        left_inner: {
            x: 17.625,
            y: 0,
            heading: 0
        },
        center: {
            x: 17.625,
            y: 0,
            heading: 0
        },
        right_inner: {
            x: 17.625,
            y: 0,
            heading: 0
        },
        right_outer: {
            x: 17.625,
            y: 0,
            heading: 0
        }
    },
    right: {
        left_outer: {
            x: 630.375,
            y: 0,
            heading: Math.PI
        },
        left_inner: {
            x: 630.375,
            y: 0,
            heading: Math.PI
        },
        center: {
            x: 630.375,
            y: 0,
            heading: Math.PI
        },
        right_inner: {
            x: 630.375,
            y: 0,
            heading: Math.PI
        },
        right_outer: {
            x: 630.375,
            y: 0,
            heading: Math.PI
        }
    }
}
var side = "left";
var position = "left_outer";


function toRadians (angle) {
    return angle * (Math.PI / 180);
}
function toDegrees (angle) {
    return angle * (180 / Math.PI);
}
function setRobotOffset (x, y, heading) {
    robot_offset.x = x;
    robot_offset.y = y;
    robot_offset.heading = heading;
    updateRobotView();
}
function setRobotOffsetSet (set) {
    robot_offset.x = set.x;
    robot_offset.y = set.y;
    robot_offset.heading = set.heading;
    updateRobotView();
}
function setRobotPosition (x, y, heading) {
    robot_position.x = x;
    robot_position.y = y;
    robot_position.heading = heading;
    updateRobotSize();
    var robot_coordinates = localToGlobalCoordinates(robot_position.x, robot_position.y);
    ui.robot.style.top = ((robot_coordinates.y + robot_offset.y + config.field_offset.y - (config.robot_height / 2)) * pixels_per_inch) + "px";
    ui.robot.style.left = ((robot_coordinates.x + robot_offset.x + config.field_offset.x - (config.robot_width / 2)) * pixels_per_inch) + "px";
    console.log(toDegrees(robot_position.heading + robot_offset.heading));
    ui.robot.style.transform = "rotate(" + toDegrees(robot_position.heading + robot_offset.heading) + "deg)";
}
function localToGlobalCoordinates (x, y) {
    var new_x = (x * Math.cos(-robot_offset.heading)) + (y * Math.sin(-robot_offset.heading));
    var new_y = -(x * Math.sin(-robot_offset.heading)) + (y * Math.cos(-robot_offset.heading));
    return {
        x: new_x,
        y: new_y
    };
}
function changeRobotPosition (dX, dY, dAngle) {
    setRobotPosition(robot_position.x + dX, robot_position.y + dY, robot_position.heading + dAngle);
}
function updateRobotView() {
    changeRobotPosition(0, 0, 0);
}
function updateRobotSize () {
    pixels_per_inch = ui.field.width / config.field_width;
    ui.robot.style.width = (pixels_per_inch * config.robot_width) + "px";
}

window.onresize = function() {
    setRobotPosition(robot_position.x, robot_position.y, robot_position.heading);
}

ui.side.onchange = function() {
    side = ui.side.value;
    updateStartConfig();
}
ui.position.onchange = function() {
    position = ui.position.value;
    updateStartConfig();
}
function updateStartConfig() {
    if(side === "left") {
        switch(position) {
            case "left_outer":
                setRobotOffsetSet(configurations.left.left_outer);
                return;
            case "left_inner":
                setRobotOffsetSet(configurations.left.left_inner);
                return;
            case "center":
                setRobotOffsetSet(configurations.left.center);
                return;
            case "right_inner":
                setRobotOffsetSet(configurations.left.right_inner);
                return;
            case "right_outer":
                setRobotOffsetSet(configurations.left.right_outer);
                return;
            default:
                return;
        }
    } else {
        switch(position) {
            case "left_outer":
                setRobotOffsetSet(configurations.right.left_outer);
                return;
            case "left_inner":
                setRobotOffsetSet(configurations.right.left_inner);
                return;
            case "center":
                setRobotOffsetSet(configurations.right.center);
                return;
            case "right_inner":
                setRobotOffsetSet(configurations.right.right_inner);
                return;
            case "right_outer":
                setRobotOffsetSet(configurations.right.right_outer);
                return;
            default:
                return;
        }
    }
}





window.onload = function() {
    setRobotOffsetSet(configurations.left.left_outer);
    setRobotPosition(0, 0, 0);
    ui.side.value = side;
    ui.position.value = position;

    var handler = new MessageHandler("ws://10.54.99.2:5804/dashboard/main");
    handler.connect(run);
}

function run(handler) {
    console.log("Connection opened");
    handler.addKeyListener("x_position", function() {
        setRobotPosition(parseFloat(handler.getProperty("y_position")), parseFloat(handler.getProperty("x_position")), toRadians(parseFloat(handler.getProperty("robot_angle"))));
    });
    handler.addKeyListener("y_position", function() {
        setRobotPosition(parseFloat(handler.getProperty("y_position")), parseFloat(handler.getProperty("x_position")), toRadians(parseFloat(handler.getProperty("robot_angle"))));
    });
    handler.addKeyListener("robot_angle", function() {
        setRobotPosition(parseFloat(handler.getProperty("y_position")), parseFloat(handler.getProperty("x_position")), toRadians(parseFloat(handler.getProperty("robot_angle"))));
    });
}