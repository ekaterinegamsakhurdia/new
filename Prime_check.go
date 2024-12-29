package main


import (
	"fmt"
)


func main() {
    fmt.Println("Enter number ")
    var input = 0
    fmt.Scanln(&input)
	var temp = 2
    var	flag = 1
    	for temp < input/2 {
    		if input % temp == 0 {
    			flag = 0
    		}
    		temp = temp + 1
    	}
    	if flag == 1 {
    		fmt.Println("The number is prime")
    	}
    	if flag == 0 {
    		fmt.Println("The number is not prime")
    	}
}