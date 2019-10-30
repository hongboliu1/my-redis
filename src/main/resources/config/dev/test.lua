local key1 = KEYS[1]
local key2 = KEYS[2]
local arg1 = ARGV[1]
local arg2 = ARGV[2]

redis.call('set',key1,arg1)
redis.call('set',key2,arg2)
return 1
