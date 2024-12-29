package main


import (
	"fmt"
)


func main() {
    fmt.Println("Enter integer N ");;
    var input = 0
    fmt.Scanln(&input)
	var factorial = 1
	for input > 1 {
		factorial = factorial * input
		input = input - 1

	}
	fmt.Println("The factorial of N is")
	fmt.Println(factorial)
}
