from ast import parse
import sys
import re

def parse_labels(lines, labels):
	line_num = 0

	for line in lines:
		line = line.replace('\n', '').replace('\r', '')
		
		if line[0] == '#':
			continue

		if line[0] == '.':
			labels[line[1:]] = line_num*4
			print("Label: " + line[1:] + " @ " + format(line_num*4, '#04x'))
		else:
			line_num = line_num + 1

def main(file):
	permitted_registers = range(0, 10)

	labels = {}

	lines = file.readlines()
	parse_labels(lines, labels)

	output = []
	
	for line in lines:
		binary_line = ""

		if line[0] == '.' or line[0] == '#':
			continue

		line = line.replace('\n', '').replace('\r', '')

		tokens = re.split(r'[, ]',line)
		operation = tokens[0]

		if operation == "LD":
			if tokens[1][0] == 'R' and int(tokens[1][1]) in permitted_registers:
				register = int(tokens[1][1])
				arg = tokens[3]

				if arg[0] == '$':
					address = int(arg[1:])
					
					binary_line = [0x02, register, address >> 8, address & 0xFF]

				elif arg[0] == 'R':
					second_register = int(arg[1:])

					binary_line = [0x01, register, 0, second_register]

				else:
					value = int(arg, 0)
					binary_line = [0x00, register, value >> 8, value & 0xFF]
			else:
				print("Invalid register")
				sys.exit()

		elif operation == "ST":
			if tokens[1][0] == 'R' and int(tokens[1][1]) in permitted_registers:
				register = int(tokens[1][1])
				arg = tokens[3]

				if arg[0] == '$':
					address = int(arg[1:])
					
					binary_line = [0x10, register, address >> 8, address & 0xFF]

				elif arg[0] == 'R':
					second_register = int(arg[1:])

					binary_line = [0x11, register, 0, second_register]

				else:
					print("Invalid operation argument")
					sys.exit()
			else:
				print("Invalid register")
				sys.exit()

		elif operation == "CMP":
			if tokens[1][0] == 'R' and int(tokens[1][1]) in permitted_registers:
				register = int(tokens[1][1])
				arg = tokens[3]

				if arg[0] == 'R':
					second_register = arg[1:]

					binary_line = [0x20, register, 0, second_register]

				else:
					value = int(arg, 0)
					binary_line = [0x21, register, value >> 8, value & 0xFF]

			else:
				print("Invalid register")
				sys.exit()
		elif operation == "BEQ":
			if tokens[1] in labels:
				address = labels[tokens[1]]

				binary_line = [0x30, 0x00, address >> 8, address & 0xFF]
			else:
				print("Unexpected label " + tokens[1])
				sys.exit()

		elif operation == "BGT":
			if tokens[1] in labels:
				address = labels[tokens[1]]

				binary_line = [0x31, 0x00, address >> 8, address & 0xFF]
			else:
				print("Unexpected label " + tokens[1])
				sys.exit()

		elif operation == "BLT":
			if tokens[1] in labels:
				address = labels[tokens[1]]

				binary_line = [0x32, 0x00, address >> 8, address & 0xFF]
			else:
				print("Unexpected label " + tokens[1])
				sys.exit()

		elif operation == "BRA":
			if tokens[1] in labels:
				address = labels[tokens[1]]

				binary_line = [0x33, 0x00, address >> 8, address & 0xFF]
			else:
				print("Unexpected label " + tokens[1])
				sys.exit()

		elif operation == "ADD":
			if tokens[1][0] == 'R' and int(tokens[1][1]) in permitted_registers:
				register = int(tokens[1][1])
				arg = tokens[3]

				if arg[0] == 'R':
					second_register = arg[1:]

					binary_line = [0x41, register, 0, second_register]

				else:
					value = int(arg, 0)
					binary_line = [0x40, register, value >> 8, value & 0xFF]

		elif operation == "SUB":
			if tokens[1][0] == 'R' and int(tokens[1][1]) in permitted_registers:
				register = int(tokens[1][1])
				arg = tokens[3]

				if arg[0] == 'R':
					second_register = arg[1:]

					binary_line = [0x43, register, 0, second_register]

				else:
					value = int(arg, 0)
					binary_line = [0x42, register, value >> 8, value & 0xFF]

		elif operation == "HALT":
			binary_line = [0xFE, 0xFE, 0xFF, 0xFF]

		elif operation == "NOOP":
			binary_line = [0xFF, 0xFF, 0xFF, 0xFF]

		else:
			print("Illegal operation")
			sys.exit()

		output.append(binary_line)
		binary_line = ""

	return output


if len(sys.argv) != 3:
	print("Usage: ASM file.asm, OUT file.bin")
	sys.exit()

input_file = open(sys.argv[1])

binary_lines = main(input_file)

output_file = open(sys.argv[2], "wb")

for line in binary_lines:
	output_file.write(bytearray(line))
