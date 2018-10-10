#!/usr/bin/env python3
import argparse
import math
import numpy
import sys
import traceback

class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    @staticmethod
    def distanceBetween(a, b):
        x = a.x - b.y
        y = a.y - b.y
        return math.hypot(x, y)

    def __str__(self):
        return "x: {}, y: {}".format(self.x, self.y)

def readPointsFromFile(file, debug_mode=False):
    points = []
    a = None
    b = None
    tolerance = None
    with open(file, "r") as f:
            line_num = 0
            for line in f:
                
                if(line[0] == "#"):
                    continue
                if line_num == 0:
                    temp = line.split(",")
                    a = float(temp[0])
                    b = float(temp[1])
                    tolerance = float(temp[2])
                    if debug_mode:
                        print("A: {}".format(a))
                        print("B: {}".format(b))
                        print("Tolerance: {}".format(tolerance))
                else:
                    temp = line.split(",")
                    x = float(temp[0])
                    y = float(temp[1])
                    p = Point(x, y)
                    if(debug_mode):
                        print("Point {}: {}".format(line_num, p))
                    points.append(p)

                line_num += 1
    return (a, b, tolerance, points)


def writePathDataToFile(file, coordinates, distances, curvature, max_velocity, target_velocity):
    if(not (len(coordinates) == len(distances) == len(curvature) == len(max_velocity) == len(target_velocity))):
        print("ERROR: arrays not the same dimensions!")
        print("cordinates: {}".format(len(coordinates)))
        print("distances: {}".format(len(distances)))
        print("curvature: {}".format(len(curvature)))
        print("max_velocity: {}".format(len(max_velocity)))
        print("target_velocity: {}".format(len(target_velocity)))

        sys.exit(1)

    with open(file, "w") as f:
        # write header
        f.write("# format: x, y, distance, curvature, max_velocity, target_velocity\n")
        for i in range(len(coordinates)):
            f.write("{},{},{},{},{},{}\n".format(coordinates[i].x, coordinates[i].y, distances[i], curvature[i], max_velocity[i], target_velocity[i]))

def injectPoints(points):
    newPath = []
    for p in points:
        
        pass
    return newPath

def smoothPoints(points, a, b, tolerance):
    newPath = []
    change = tolerance
    while change >= tolerance:
        change = 0.0
        for i in range(1, len(points) - 1):
            #aux = 
            pass
    return newPath

def calculateDistancesAlongPath(points):
    distances = [0]
    for i in range(1, len(points)):
        length = Point.distanceBetween(points[i], points[i - 1])
        distances.append(distances[i - 1] + length)
    return distances

def calculateCurvature(points):
    curvature = [0]
    for i in range(1, len(points) - 1):
        # P: current point (x1, y1)
        x1 = points[i].x
        y1 = points[i].y
        # Q: point behind (x2, y2)
        x2 = points[i - 1].x
        y2 = points[i - 1].y
        # R: point infront (x3, y3)
        x3 = points[i + 1].x
        y3 = points[i + 1].y

        if x1 == x2:
            x1 += 1E-10

        k1 = 0.5 * (x1**2 + y1**2 - x2**2 - y2**2) / (x1 - x2)
        k2 = (y1 - y2) / (x1 - x2)
        b =  0.5 * (x1**2 - 2.0 * x2 * k1 + y2**2 - x3**2 + 2.0 * x3 * k1 - y3**2)
        a = k1 - k2 * b
        r = math.hypot(x1 - a, y1 - b)
        print("R: {}".format(r))
        curvature.append(1.0 / r)


    curvature.append(0)
    return curvature

def calculateMaxVelocity(curvature):
    res = []
    for i in range(len(curvature)):
        res[i] = min(2 / curvature[i], )

def main(): 
    parser = argparse.ArgumentParser()
    parser.add_argument("-i", "--input-file", help="file to get original coordinates from", type=str, required=True)
    parser.add_argument("-o", "--output-file", help="file to output data to", type=str, required=True)
    parser.add_argument("-d", "--debug", help="debug mode. 1 to turn on", type=int, required=False)
    args = parser.parse_args()
    debug_mode = False
    if(args.debug == 1):
        debug_mode = True
    if debug_mode:
        print("Input file:", args.input_file)
        print("Output file:", args.output_file)
    input_file = args.input_file
    output_file = args.output_file

    try:
        
        (a,b, tolerance, path) = readPointsFromFile(input_file, debug_mode)
        #path = injectPoints(path)
        #path = smoothPoints(path, a, b, tolerance)
        distances = calculateDistancesAlongPath(path)
        curvatures = calculateCurvature(path)
        max_velocity = []
        target_velocity = []
        for i in range(len(path)):
            #distances.append(0)
            #curvatures.append(0)
            max_velocity.append(0)
            target_velocity.append(0)
        
        
        writePathDataToFile(output_file, path, distances, curvatures, max_velocity, target_velocity)
        print("Finished!")
    except Exception as e:
        print(e.with_traceback(True))
        # sys.exit(1)

if __name__ == "__main__":
    main()
