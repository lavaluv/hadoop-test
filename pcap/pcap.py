# coding=utf-8
import copy
import PcapAnalyzer.protocol as protocol

inputFilePath = 'test.pcap'
outputFilePath = 'result.txt'

fpcap = open(inputFilePath,'rb')
file = open(outputFilePath,'w')
input_data = fpcap.read()

#pcap header
pHeader = protocol.Pcap(input_data[0:24])
pHeader.writeIntoFile(file,'magicNum','verMajor','verMinor','thiszone','sigfigs','snaplen','linktype')
#data
pDataArray = []
i = 24

while (i < len(input_data)):
	#dataHeader
	pData = protocol.PcapData(input_data[i:])
	#write into pData
	pDataArray.append(copy.deepcopy(pData))
	i = i + pData.getValue('caplen') + 16
#pcap data packet
for data in pDataArray:
	data.writeIntoFile(file,'GMTTime','microTime','caplen','datalen','content')

file.write('Have'+str(len(pDataArray))+"pcakets"+'\n')
file.close()
fpcap.close()