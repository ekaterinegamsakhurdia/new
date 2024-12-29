package main


import (
	"fmt"
)


func main() {
    fmt.Println("Enter number ")
    var x = 0
    fmt.Scanln(&x)
	var reversed = 0
	var temp = x
	for x != 0 {
		reversed = 10 * reversed + x % 10
		x = x / 10
	}
	if reversed == temp {
		fmt.Println("palindrome!")
	}
	if reversed != temp {
		fmt.Println("not palindrome...")
	}
}