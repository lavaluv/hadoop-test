import struct

class protocolStruct(object):
	"""docstring for protocolStruct"""
	def __init__(self, source):
		super(protocolStruct, self).__init__()
	def getValue(self,alt):
		return self.__getattribute__(alt)
	def writeIntoFile(self,file,*alt):
		for key in alt:
			file.write(key+ " : " + repr(self.getValue(key))+'\n')
		
class Pcap(protocolStruct):
	def __init__(self,input):
		self.magicNum = input[0:4]
		self.verMajor = input[4:6]
		self.verMinor = input[6:8]
		self.thiszone = input[8:12]
		self.sigfigs = input[12:16]
		self.snaplen = input[16:20]
		self.linktype = input[20:24]

class PcapData(protocolStruct):
	def __init__(self,input):
		self.GMTTime = struct.unpack('I',input[0:4])[0]
		self.microTime = struct.unpack('I',input[4:8])[0]
		self.caplen = struct.unpack('I',input[8:12])[0]
		self.datalen = struct.unpack('I',input[12:16])[0]
		self.content = input[16:16+self.caplen]
		
def main():
	fpcap = open('../test.pcap','rb')
	input_data = fpcap.read()
	pcap = Pcap(input_data[0:24])
	pcap.writeIntoFile('magicNum','verMajor')
	pass
if __name__ == '__main__':
	main()