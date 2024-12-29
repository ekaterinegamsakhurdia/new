package main


import (
	"fmt"
)


func main() {
    fmt.Println("Enter number ")
    var x = 0
    fmt.Scanln(&x)
	var reversed = 0
	for x != 0 {
		reversed = 10 * reversed + x % 10
		x = x / 10
	}
	fmt.Println("Reversed number ")
	fmt.Println(reversed)
}