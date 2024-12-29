package main


import (
	"fmt"
)


func main() {
    var x = 0
    fmt.Println("Enter a number")
    fmt.Scanln(&x)
	var max = 0
	for x != 0 {
		if x % 10 > max {
			max = x % 10
		}
		x = x / 10
	}
	fmt.Println("The largest digit is ")
	fmt.Println(max)
}
// github gaxseni aaba


