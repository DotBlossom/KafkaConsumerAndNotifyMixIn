components/notification?
==> export to GloballayoutComponents<&&isLogined, NavBar?>

max-notifiaction 개수?
login - jwt read.


    useEffect(()=> {
        const eventSource = new EventSource("http://${ElasticIP}/notifications/${userId}")
    
        eventSource.onopen = async () => {
          await console.log("sse opened!")
        }
    
        eventSource.addEventListener('type1', (event) => {
          console.log("type1")
          const data = JSON.parse(event.data);
          console.log(data)
        });
        
        eventSource.addEventListener('type2', (event) => {
          console.log("type2")
          const data = JSON.parse(event.data);
          console.log(data)
        });
        
        eventSource.addEventListener('type3', (event) => {
          console.log("type3")
          const data = JSON.parse(event.data);
          console.log(data)
        });
    
        eventSource.onerror = async (e) => {
          await console.log(e)
        }
        
        // if notification component is unmounted
        return () => {
          eventSource.close()
        }
    },[])
    
    return (
        render...
        notifications.map(n, i) ...
    )